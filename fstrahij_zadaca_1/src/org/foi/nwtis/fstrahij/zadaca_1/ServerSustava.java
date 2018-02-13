/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.zadaca_1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.Thread.sleep;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.fstrahij.konfiguracije.Konfiguracija;
import org.foi.nwtis.fstrahij.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.fstrahij.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.fstrahij.konfiguracije.NemaKonfiguracije;

/**
 * Prvo se provjeravaju upisane opcije, koristeći dopuštene izraze. Učitavaju se
 * postavke iz datoteke. Server kreira i pokreće nadzornu dretvu (klasa
 * NadzorDretvi), kreira i pokreće adresnu dretvu (klasa ProvjeraAdresa), kreira
 * i pokreće rezervnu dretvu (klasa RezervnaDretva), kreira i pokreće dretvu za
 * serijalizaciju evidencije (klasa SerijalizatorEvidencije). Otvara se
 * ServerSocket na izabranom portu i čeka zahtjev korisnika u beskonačnoj
 * petlji. Kada se korisnik spoji na otvorenu vezu, kreira se objekt dretve
 * klase RadnaDretva, veza se predaje objektu i pokreće se izvršavanje dretve.
 * Dretve opslužuju zahtjev korisnika. Dretva nakon što obradi pridruženi
 * zahtjev korisnika završava svoj rad i briše se.
 *
 * @author Filip
 */
public class ServerSustava {

    private Evidencija evidencija = new Evidencija();

    public static void main(String[] args) {
        //-konf datoteka(.txt | .xml) [-load]
        String sintaksa = "^-konf ([^\\s]+\\.(?i))(txt|xml|bin)( +-load)?$";

        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg).append(" ");
        }
        String p = sb.toString().trim();
        Pattern pattern = Pattern.compile(sintaksa);
        Matcher m = pattern.matcher(p);
        boolean status = m.matches();
        if (status) {
            int poc = 0;
            int kraj = m.groupCount();
            for (int i = poc; i <= kraj; i++) {
                System.out.println(i + ". " + m.group(i));
            }

            String nazivDatoteke = m.group(1) + m.group(2);
            boolean trebaUcitatiEvidenciju = false;
            if (m.group(3) != null) {
                trebaUcitatiEvidenciju = true;
            }

            ServerSustava server = new ServerSustava();
            server.pokreniServer(nazivDatoteke, trebaUcitatiEvidenciju);

        } else {
            System.out.println("ERROR 90; Ne odgovara!");
        }
    }

    /**
     * Provjerava postoji li evidDatoteka
     *
     * @param konfig
     * @param trebaUcitatiEvidenciju
     */
    private void provjeriEvidDatoteku(Konfiguracija konfig, boolean trebaUcitatiEvidenciju) {
        if (trebaUcitatiEvidenciju) {
            String datotekaEvidencije = konfig.dajPostavku("evidDatoteka");
            File evidDat = new File(datotekaEvidencije);
            if (evidDat.exists() && !evidDat.isDirectory()) {
                System.out.println(datotekaEvidencije + " postoji");

            } else {
                System.out.println(datotekaEvidencije + " ne postoji");
            }
        }
    }

    /**
     * Serijalizira podatke u evidDatoteku
     *
     * @param datoteka
     */
    private void serijalizirajEvidencijuUDatoteku(String datoteka) {

        try {
            FileOutputStream out = new FileOutputStream(datoteka);
            ObjectOutputStream stream = new ObjectOutputStream(out);

            stream.writeObject(evidencija);
            stream.close();
            out.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * kreira i pokreće nadzornu dretvu (klasa NadzorDretvi), kreira i pokreće
     * adresnu dretvu (klasa ProvjeraAdresa), kreira i pokreće rezervnu dretvu
     * (klasa RezervnaDretva), kreira i pokreće dretvu za serijalizaciju
     * evidencije (klasa SerijalizatorEvidencije). Otvara se ServerSocket na
     * izabranom portu i čeka zahtjev korisnika u beskonačnoj petlji. Kada se
     * korisnik spoji na otvorenu vezu, kreira se objekt dretve klase
     * RadnaDretva, veza se predaje objektu i pokreće se izvršavanje dretve.
     * Dretve opslužuju zahtjev korisnika.
     *
     * @param nazivDatoteke
     * @param trebaUcitatiEvidenciju
     */
    private void pokreniServer(String nazivDatoteke, boolean trebaUcitatiEvidenciju) {
        //TODO kreirati kolekciju u kojoj Ä‡e se spremati aktivne dretve
        ArrayList<Thread> kolekcijaAktivnihDretvi = new ArrayList<>();

        try {
            Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(nazivDatoteke);
            provjeriEvidDatoteku(konfig, trebaUcitatiEvidenciju);

            int port = Integer.parseInt(konfig.dajPostavku("port"));
            int maksAdresa = Integer.parseInt(konfig.dajPostavku("maksAdresa"));
            int maksBrojRadnihDretvi = Integer.parseInt(konfig.dajPostavku("maksBrojRadnihDretvi"));
            String datotekaEvidencije = konfig.dajPostavku("evidDatoteka");
            short brojacRadnihDretvi = 0;
            brojacRadnihDretvi = (short) Short.toUnsignedInt(brojacRadnihDretvi);
            evidencija.setBrojPrekinutihZahtjeva(5);
            evidencija.setBrojUspjesnihZahtjeva(5);
            evidencija.setUkupnoZahtjeva(25);
            evidencija.setZahtjeviZaAdrese(null);
            serijalizirajEvidencijuUDatoteku(datotekaEvidencije);

            NadzorDretvi nd = new NadzorDretvi(konfig);
            nd.start();
            RezervnaDretva rezervnaDretva = new RezervnaDretva(konfig);
            rezervnaDretva.start();
            ProvjeraAdresa pa = new ProvjeraAdresa(konfig);
            pa.start();
            SerijalizatorEvidencije se = new SerijalizatorEvidencije(konfig);
            se.start();

            ServerSocket serverSocket = new ServerSocket(port);

            while (true) {
                Socket socket = serverSocket.accept();

                //TODO dodaj dretvu u kolekciju aktivnih radnih dretvi
                if (brojacRadnihDretvi < maksBrojRadnihDretvi) {
                    brojacRadnihDretvi++;
                } else {
                    System.out.println("nema mjesta za radnu dretvu");
                    break;
                }
                RadnaDretva rd = new RadnaDretva(socket, konfig.dajPostavku("adminDatoteka"));

                String nazivDretve = rd.getName();
                nazivDretve += "-" + brojacRadnihDretvi;
                rd.setName(nazivDretve);

                kolekcijaAktivnihDretvi.add(rd);
                rd.start();

                for (Iterator<Thread> iterator = kolekcijaAktivnihDretvi.iterator(); iterator.hasNext();) {
                    Thread next = iterator.next();
                    if (next.getState() == Thread.State.TERMINATED) {
                        kolekcijaAktivnihDretvi.remove(next);
                        brojacRadnihDretvi--;
                    }
                }
                //TODO treba provjeriti ima li "mjesta" za novu radnu dretvu
            }

        } catch (NemaKonfiguracije | NeispravnaKonfiguracija | IOException ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
