/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.socket.server;

import org.foi.nwtis.fstrahij.socket.server.PreuzmiMeteoPodatke;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.foi.nwtis.fstrahij.baza.PreuzmiPodatkeIzBaze;
import org.foi.nwtis.fstrahij.konfiguracije.Konfiguracija;
import org.foi.nwtis.fstrahij.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.fstrahij.web.podaci.Korisnik;
import org.foi.nwtis.fstrahij.web.podaci.Uredjaj;

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
    private String porukaKorisniku, datoteka;
    private String korisnickoIme, vrstaZahtjeva;
    private String uredjaji = null;

    //TODO varijabla za vrijeme poÄŤetka rada dretve -R    
    public RadnaDretva(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        poc = System.currentTimeMillis();

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
            long startTime = System.currentTimeMillis();
            uredjaji = null;
            switch (provjeriNaredbu(sb)) {
                case -2:
                    porukaKorisniku = "Admin je u stanju PAUSE";
                    break;
                case -1:
                    porukaKorisniku = "ERR 11;";
                    break;
                case 0:
                    porukaKorisniku = "OK 10";
                    break;
                case 1:
                    porukaKorisniku = "ERR 10";
                    break;
                case 2:
                    porukaKorisniku = "ERR 12;";
                    break;
                case 3:
                    porukaKorisniku = "OK 13;";
                    break;
                case 4:
                    porukaKorisniku = "OK 14;";
                    break;
                case 5:
                    porukaKorisniku = "OK 15;";
                    break;
                case 13:
                    porukaKorisniku = "ERR 13; Dretva nije uspjesno odradila cekanje";
                    break;
                case 20:
                    porukaKorisniku = "ERR 20;";
                    break;
                case 21:
                    porukaKorisniku = "ERR 21;";
                    break;
                case 22:
                    porukaKorisniku = "ERR 22;";
                    break;
                case 23:
                    porukaKorisniku = "ERR 23;";
                    break;
                case 24:
                    porukaKorisniku = "BLOKIRANA";
                    break;
                case 25:
                    porukaKorisniku = "AKTIVNA";
                    break;
                case 30:
                    porukaKorisniku = "ERR 30;";
                    break;
                case 31:
                    porukaKorisniku = "ERR 31;";
                    break;
                case 32:
                    porukaKorisniku = "ERR 32;";
                    break;
                case 33:
                    porukaKorisniku = "ERR 33;";
                    break;
                default:
                    porukaKorisniku = "default";
            }

            long trajanjeObrade = System.currentTimeMillis();
            trajanjeObrade = trajanjeObrade - startTime;
            int trajanje = (int) trajanjeObrade;
            unesiUBazuZahtjev();
            unesiUDnevnikRada(trajanje);
            
            if (uredjaji != null) {
                os.write(uredjaji.getBytes());
                os.flush();
            } else {
                os.write(porukaKorisniku.getBytes());
                os.flush();
            }

        } catch (IOException | URISyntaxException | InterruptedException ex) {
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
     * @param korisnik
     * @param lozinka
     * @return true ako postoji ili false ako ne postoji
     */
    private boolean provjeriKorisnika(String korisnik, String lozinka) {
        List<Korisnik> korisnici = new ArrayList<>();
        String sql = "SELECT * FROM KORISNIK WHERE korIme='" + korisnik + "' AND lozinka='" + lozinka + "'";
        PreuzmiPodatkeIzBaze podaci = new PreuzmiPodatkeIzBaze();
        korisnici = podaci.dajKorisnik(sql);

        if (korisnici.isEmpty()) {
            return false;
        }
        korisnickoIme = null;
        korisnickoIme = korisnik;
        return true;
    }

    /**
     * Služi za provjeru dobivenog zahtjeva od korisnika Koriste se dopušteni
     * izrazi vraća integer koji služi za ispis poruke korisniku
     *
     * @param sb
     * @return integer
     */
    private int provjeriNaredbu(StringBuffer sb) throws URISyntaxException, InterruptedException, UnsupportedEncodingException {
        String sintaksa_korisnik = "^USER ([a-zA-Z0-9_.-]{1,}); PASSWD ([a-zA-Z0-9_.-.!.#]{1,}); (PAUSE;|START;|STOP;|STATUS;)?$";
        String iot_master = "^USER ([a-zA-Z0-9_.-]{1,}); PASSWD ([a-zA-Z0-9_.-.!.#]{1,}); (IoT_Master) (START;|STOP;|WORK;|WAIT;|LOAD;|CLEAR;|STATUS;|LIST;)?$";
        String iot = "^USER ([a-zA-Z0-9_.-]{1,}); PASSWD ([a-zA-Z0-9_.-.!.#]{1,}); (IoT) ([0-9]{1,6}) (ADD \\\"([a-zA-Z0-9_.-.!.# -]{1,})\\\" ([a-zA-Z0-9_.-.!.# ščžćđ]{1,}, [a-zA-Z0-9_.-.!.#]{1,});|STOP;|WORK;|WAIT;|REMOVE;|STATUS;)?$";

        Pattern p = Pattern.compile(sintaksa_korisnik);
        Matcher m = p.matcher(sb);
        boolean status = m.matches();

        Pattern p2 = Pattern.compile(iot_master);
        Matcher m2 = p2.matcher(sb);
        boolean status2 = m2.matches();

        Pattern p3 = Pattern.compile(iot);
        Matcher m3 = p3.matcher(sb);
        boolean status3 = m3.matches();
        if (status) {
            if (provjeriKorisnika(m.group(1), m.group(2))) {
                System.out.println("korisnik postoji!");
            } else {
                return 1;
            }
            //TODO dobrĹˇiti za admina -R   
            vrstaZahtjeva = m.group(3);
            switch (m.group(3)) {
                case "PAUSE;":
                    return userPause();
                case "STOP;":
                    return userStop();
                case "START;":
                    return userStart();
                case "STATUS;":
                    return userStatus();
            }

        } else if (status2) {
            if (provjeriKorisnika(m2.group(1), m2.group(2))) {
                System.out.println("korisnik postoji!");
            } else {
                return 1;
            }

            if (m2.group(3).equals("IoT_Master")) {
                vrstaZahtjeva = m2.group(3) + "-" + m2.group(4);
                IoTMaster itm = new IoTMaster();
                switch (m2.group(4)) {
                    case "START;":
                        return itm.Start();
                    case "STOP;":
                        return itm.Stop();
                    case "WORK;":
                        return itm.Work();
                    case "WAIT;":
                        return itm.Wait();
                    case "STATUS;":
                        return itm.StatusKorisnika();
                     case "LOAD;":
                        return itm.Load();
                    case "CLEAR;":
                        return itm.Clear();
                    case "LIST;":
                        uredjaji = itm.List();
                        return 0;
                }
            }

        } else if (status3) {
            if (m3.group(5).startsWith("ADD")) {
                String add = m3.group(5).substring(0, 3);
                vrstaZahtjeva = m3.group(3) + "-" + add;
                int id = Integer.parseInt(m3.group(4));
                String naziv = m3.group(6);
                String adresa = new String(m3.group(7).getBytes("ISO-8859-1"), "UTF-8");
                
                IoT it = new IoT();
                return it.Add(id, naziv, adresa);
            } else {
                vrstaZahtjeva = m3.group(3) + "-" + m3.group(5);
            }
            IoT it = new IoT();
            int id = Integer.parseInt(m3.group(4));
            switch (m3.group(5)) {
                case "WORK;":
                    return it.Work(id);
                case "WAIT;":
                    return it.Wait(id);
                case "REMOVE;":
                    return it.Remove(id);
                case "STATUS;":
                    return it.StatusUredjaja(id);
            }
        } else {
            System.out.println("neispravna sintaksa");
        }

        return 0;
    }

    /**
     * obrada korisničkog zahtjeva PAUSE
     *
     * @return 1 ako je dretva u TIMED_WAITING stanju ili 0 ako nije
     */
    private int userPause() {
        /*for (Iterator<Thread> iterator = kolekcijaAdmin.iterator(); iterator.hasNext();) {
            Thread next = iterator.next();
            if (next.getState() == State.TIMED_WAITING) {
                zastavica = true;
                return 1;
            }
        }
        return 0;*/

        if (SlusacAplikacije.op.pause) {
            return 1;
        } else {
            System.out.println("ulaz u pauziranje");
            SlusacAplikacije.op.pause = true;
            return 0;
        }
    }

    /**
     * obrada korisničkog zahtjeva START
     *
     * @return 2 ako je dretva u TIMED_WAITING stanju ili 0 ako nije
     */
    private int userStart() {
        if (SlusacAplikacije.op.pause) {

            SlusacAplikacije.op.pokreniCekanje();
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * obrada korisničkog zahtjeva STOP
     *
     * @return 0 ako se server ispravno zaustavio ili 3 ako nije
     */
    public int userStop() {

        userPause();
        if (SlusacAplikacije.op.isInterrupted()) {
            SlusacAplikacije.op.zaustaviDretvu();
            return 2;
        }
        SlusacAplikacije.op.zaustaviDretvu();
        return 0;
        /*interrupt();
        try {
            socket.close();
        } catch (IOException ex) {
            return 3;
        }
        System.exit(0);
        return 0;*/
    }

    /**
     * obrada korisničkog zahtjeva STAT
     *
     * @return 4
     */
    private int userStatus() {
        long startTime = System.currentTimeMillis();
        Thread.State state = SlusacAplikacije.op.getState();
        switch (state) {
            case WAITING:
                System.out.println("čeka");
                return 3;
            case TERMINATED:
                System.out.println("gotova s radom");
                return 5;
            default:
                System.out.println("startana");
                return 4;
        }
    }

    private void unesiUDnevnikRada(int trajanje) throws MalformedURLException, UnknownHostException {
        URL url = new URL("http://localhost:8080/fstrahij_aplikacija_2_web/faces/pogled_3.xhtml");
        InetAddress address = InetAddress.getByName(url.getHost());
        String temp = address.toString();
        String IP = temp.substring(temp.indexOf("/") + 1, temp.length());

        PreuzmiPodatkeIzBaze podaci = new PreuzmiPodatkeIzBaze();
        String sql = "INSERT INTO DNEVNIK (korisnik,url,ipadresa,trajanje) values ('" + korisnickoIme + "', '" + url.toString() + "', '" + IP + "', " + trajanje + ")";
        podaci.noviZapisDnevnikRada(sql);
    }

    private void unesiUBazuZahtjev() {
        PreuzmiPodatkeIzBaze podaci = new PreuzmiPodatkeIzBaze();
        String sql = "INSERT INTO ZAHTJEVI (korisnik,vrsta) values ('" + korisnickoIme + "', '" + vrstaZahtjeva + "')";
        podaci.tablicaKorisnikInt(sql);
    }
}
