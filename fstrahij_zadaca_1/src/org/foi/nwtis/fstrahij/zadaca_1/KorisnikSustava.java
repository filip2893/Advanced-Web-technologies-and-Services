/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.zadaca_1;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *Prvo se provjeravaju upisane opcije, preporučuje se koristiti dopuštene izraze. 
 * Na temelju opcije kreira se objekt potrebne klase AdministratorSustava, KlijentSustava ili 
 * PregledSustava, te se nastavlja s izvršavanjem tog objekta.
 * @author Filip
 */
public class KorisnikSustava {
/** 
 * @param args 
 */
    public static void main(String[] args) {
        //-admin -server [ipadresa | adresa] -port port -u korisnik -p lozinka [-pause | -start | -stop | -stat ]
        //TODO dovrÅ¡i ostale paremetre
        //-admin -server localhost -port 8000
        String ipv4 = "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
        String sintaksaAdmin = "^-admin -server (" + ipv4 + "|([a-zA-Z0-9_.-]{1,})) -port ([0-9]{4})? -u ([a-zA-Z0-9_.-]{1,}) -p ([a-zA-Z0-9_.-.!.#]{1,})( -pause| -start| -stop| -stat)?$";
        String sintaksaKorisnik = "^-korisnik -s (" + ipv4 + "|([a-zA-Z0-9_.-]{1,})) -port ([0-9]{4})? -u ([a-zA-Z0-9_.-]{1,})(( -a| -t) ((https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|])| -w ([0-9]{1,}))$";
        String sintaksaPrikaz = "^-prikaz -s (([a-zA-Z0-9_.-.!.#]{1,})|(([a-zA-Z]:)?(\\\\[a-zA-Z0-9_.-]+)+\\\\?)|((https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]))$";

        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg).append(" ");
        }
        String p = sb.toString().trim();
        Pattern pattern;
        Matcher m;
        boolean status;
        switch (args[0]) {
            case "-admin":
                pattern = Pattern.compile(sintaksaAdmin);
                m = pattern.matcher(p);
                status = m.matches();
                if (status) {
                    String nazivServera = m.group(1);
                    int port = Integer.parseInt(m.group(7));
                    if (port < 8000) {
                        System.out.println("ERROR 90; Ne odgovara regex Admin!");
                        return;
                    }

                    String korIme = m.group(8);
                    String lozinka = m.group(9);
                    String komanda = m.group(10).substring(2).toUpperCase();

                    AdministratorSustava administratorSustava = new AdministratorSustava();
                    administratorSustava.pokreniAdministratora(nazivServera, port, korIme, lozinka, komanda);
                } else {
                    System.out.println("ERROR 90; Ne odgovara regex Admin!");
                }
                break;
            case "-korisnik":
                pattern = Pattern.compile(sintaksaKorisnik);
                m = pattern.matcher(p);
                status = m.matches();
                if (status) {
                    String nazivServera = m.group(1);
                    int port = Integer.parseInt(m.group(7));
                    String korIme = m.group(8);
                    String komanda = null, komandaKlijent = null;
                    if (m.group(9) != null && m.group(9).contains(" -a")) {
                        komanda = "ADD";
                        komandaKlijent = m.group(11);
                    } else if (m.group(9) != null && m.group(9).contains(" -t")) {
                        komanda = "TEST";
                        komandaKlijent = m.group(11);
                    } else if (m.group(9) != null && m.group(9).contains(" -w")) {
                        komanda = "WAIT";
                        komandaKlijent = m.group(13);
                        if (Integer.parseInt(komandaKlijent) < 1 || Integer.parseInt(komandaKlijent) > 600) {
                            System.out.println("ERROR 90; Ne odgovara regex Korisnik!");
                            return;
                        }
                    }

                    KlijentSustava klijentSustava = new KlijentSustava();
                    klijentSustava.pokreniKlijenta(nazivServera, port, korIme, komanda, komandaKlijent);

                } else {
                    System.out.println("ERROR 90; Ne odgovara regex Korisnik!");
                }
                break;
            case "-prikaz":
                pattern = Pattern.compile(sintaksaPrikaz);
                m = pattern.matcher(p);
                status = m.matches();
                if (status) {
                    String datoteka = m.group(1);
                    PregledSustava pregledSustava = new PregledSustava();
                    pregledSustava.pokreniPregled(datoteka);

                } else {
                    System.out.println("ERROR 90; Ne odgovara regex Prikaz!");
                }
                break;
            default:
                System.out.println("ERROR 90; Krivi unos");
        }

    }
}
