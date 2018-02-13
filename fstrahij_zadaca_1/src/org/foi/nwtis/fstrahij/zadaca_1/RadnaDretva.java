/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.zadaca_1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Kreiranje konstruktora klase i metode za prijenos potrebnih podataka. Dretva
 * iz dobivene veze na socketu preuzima tokove za ulazne i izlazne podatke prema
 * korisniku. Dretva preuzima podatke koje šalje korisnik putem ulaznog toka
 * podataka, provjerava korektnost komandi iz zahtjeva. Koriste se dopušteni
 * izrazi. Na kraju dretva šalje podatke korisniku putem izlaznog toka podataka.
 * Za svaku vrstu komande kreirana je posebna metoda koja odrađuje njenu
 * funkcionalnost.
 *
 * @author Filip
 */
public class RadnaDretva extends Thread {

    private Socket socket;
    private long poc;
    public static ArrayList<Thread> kolekcijaAdmin = new ArrayList<>();
    public static boolean zastavica = false;
    private String porukaKorisniku, datoteka;

    //TODO varijabla za vrijeme poÄŤetka rada dretve -R    
    RadnaDretva(Socket socket, String datoteka) {
        this.socket = socket;
        this.datoteka = datoteka;
    }

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        poc = System.currentTimeMillis();
        //TODO preuzeti trenutno vrijeme u milisekundama -R
        System.out.println(this.getClass());
        InputStream is = null;
        OutputStream os = null;
        porukaKorisniku = null;

        try {
            is = socket.getInputStream();
            os = socket.getOutputStream();

            StringBuffer sb = new StringBuffer();
            while (true) {
                int znak = is.read();
                if (znak == -1) {
                    break;
                }
                sb.append((char) znak);
            }
            System.out.println("Primljena naredba: " + sb);

            //TODO provjeri ispravnost pripremljenog zahtjeva
            switch (provjeriNaredbu(sb)) {
                case -2:
                    porukaKorisniku = "Admin je u stanju PAUSE";
                    break;
                case -1:
                    porukaKorisniku = "ERROR 90; Neispravan unos";
                    break;
                case 0:
                    porukaKorisniku = "OK;";
                    break;
                case 1:
                    porukaKorisniku = "ERROR 01; vec postoji stanje PAUSE";
                    break;
                case 2:
                    porukaKorisniku = "ERROR 02; nije u stanju PAUSE";
                    break;
                case 3:
                    porukaKorisniku = "ERROR 03; nešto nije u redu s prekidom rada ili serijalizacijom";
                    break;
                case 13:
                    porukaKorisniku = "ERROR 13; Dretva nije uspjesno odradila cekanje";
                    break;
                case 100:
                    porukaKorisniku = "ERROR 00; neispravno korisnicko ime i/ili lozinka";
                    break;
                default:
                    porukaKorisniku = "default";
            }
            os.write(porukaKorisniku.getBytes());
            os.flush();

        } catch (IOException ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //TODO obrisati dretvu iz kolekcije aktivnih radnih dretvi
        //TODO smanjiti brojaÄŤ aktivnih radnih dretvi
        //TODO aĹľurirati evidenciju rada
    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Služi za provjeru korisničkog imena i lozinke u datoteci
     *
     * @param korIme
     * @param lozinka
     * @return true ako postoji ili false ako ne postoji
     */
    private boolean provjeriKorisnika(String korIme, String lozinka) {
        File dat = new File(datoteka);
        try (BufferedReader br = new BufferedReader(new FileReader(dat))) {
            String redak;
            while ((redak = br.readLine()) != null) {
                String[] korisnik = redak.split(";");
                if (korIme.equals(korisnik[0]) && lozinka.equals(korisnik[1])) {
                    return true;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Služi za provjeru dobivenog zahtjeva od korisnika Koriste se dopušteni
     * izrazi vraća integer koji služi za ispis poruke korisniku
     *
     * @param sb
     * @return integer
     */
    private int provjeriNaredbu(StringBuffer sb) {
        String sintaksa_admin = "^USER ([^\\s]+); PASSWD ([^\\s]+); (PAUSE|STOP|START|STAT);$";
        String sintaksa_korisnik = "USER ([^\\s]+); (ADD|TEST|WAIT) ([^\\s]+);";
        Pattern p = Pattern.compile(sintaksa_admin);
        Matcher m = p.matcher(sb);
        boolean status = m.matches();
        if (status) {
            Thread.currentThread().setName(m.group(1));
            kolekcijaAdmin.add(Thread.currentThread());
            if (provjeriKorisnika(m.group(1), m.group(2))) {
                System.out.println("korisnik postoji!");
            } else {
                return 100;
            }
            //TODO dobrĹˇiti za admina -R            
            switch (m.group(3)) {
                case "PAUSE":
                    if (adminPause() == 0) {
                        try {
                            Thread.sleep(20000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        zastavica = false;
                    } else {
                        return adminPause();
                    }
                case "STOP":
                    return adminStop();
                case "START":
                    return adminStart();
                case "STAT":
                    return adminStat();
            }
        } else {
            p = Pattern.compile(sintaksa_korisnik);
            m = p.matcher(sb);
            status = m.matches();
            if (status && !zastavica) {
                Thread.currentThread().setName(m.group(1));
                int pocetak = m.group().indexOf(";");
                pocetak += 2;
                int kraj = pocetak + 4;
                String naredba = m.group().substring(pocetak, kraj);
                switch (naredba) {
                    case "ADD ":
                        return userAdd(m.group(3));
                    case "TEST":
                        return userTest(m.group(3));
                    case "WAIT":
                        return userWait(m.group(3));
                }
            } else {
                return -1;
            }
        }
        return 0;
    }

    /**
     * obrada korisničkog zahtjeva PAUSE
     *
     * @return 1 ako je dretva u TIMED_WAITING stanju ili 0 ako nije
     */
    private int adminPause() {
        for (Iterator<Thread> iterator = kolekcijaAdmin.iterator(); iterator.hasNext();) {
            Thread next = iterator.next();
            if (next.getState() == State.TIMED_WAITING) {
                zastavica = true;
                return 1;
            }
        }
        return 0;
    }

    /**
     * obrada korisničkog zahtjeva START
     *
     * @return 2 ako je dretva u TIMED_WAITING stanju ili 0 ako nije
     */
    private int adminStart() {
        if (adminPause() == 1) {
            interrupt();
            zastavica = false;
            return 0;
        }
        return 2;
    }

    /**
     * obrada korisničkog zahtjeva STOP
     *
     * @return 0 ako se server ispravno zaustavio ili 3 ako nije
     */
    public int adminStop() {
        interrupt();
        try {
            socket.close();
        } catch (IOException ex) {
            return 3;
        }
        System.exit(0);
        return 0;
    }

    /**
     * obrada korisničkog zahtjeva STAT
     *
     * @return 4
     */
    private int adminStat() {
        return 4;
    }

    /**
     * obrada korisničkog zahtjeva ADD
     *
     * @param adresa
     * @return -2 ako je u stanju PAUSE ili 0 ako nije
     */
    public int userAdd(String adresa) {
        if (adminPause() == 1) {
            return -2;
        }
        return 0;
    }

    /**
     * obrada korisničkog zahtjeva TEST
     *
     * @param adresa
     * @return -2 ako je u stanju PAUSE ili 0 ako nije
     */
    private int userTest(String adresa) {
        if (adminPause() == 1) {
            return -2;
        }
        return 0;
    }

    /**
     * obrada korisničkog zahtjeva WAIT
     *
     * @param sec
     * @return -2 ako je u stanju PAUSE ili 0 ako nije te 13 ako je greša
     */
    private int userWait(String sec) {
        if (adminPause() == 1) {
            return -2;
        }

        int secInt = Integer.parseInt(sec);
        secInt = secInt * 1000;

        try {
            Thread.sleep(secInt);
        } catch (InterruptedException ex) {
            return 13;
        }
        return 0;
    }
}
