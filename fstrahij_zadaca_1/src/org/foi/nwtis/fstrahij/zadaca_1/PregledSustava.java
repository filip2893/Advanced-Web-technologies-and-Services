/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.zadaca_1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Prvo se provjeravaju upisane opcije, pomoću dopuštenih izraze. Otvara i čita
 * datoteku sa serijaliziranim podacima evidencije i ispisuje ih u
 * prikladnom/čitljivom i formatiranom obliku na ekran/standardni izlaz
 * korisnika.
 *
 * @author Filip
 */
public class PregledSustava {

    /**
     * Dohvaća naziv datoteke te provjerava ispravnost pomoću regexa i pokreće
     * deserijalizaciju podataka iz datoteke
     *
     * @param datoteka
     */
    public void pokreniPregled(String datoteka) {
        if (provjeriUnos("-prikaz -s " + datoteka)) {
            deserijalizirajEvidDatoteku(datoteka);
        } else {
            System.out.println("ERROR 90; Ne odgovara regex");
        }

    }

    /**
     * Provjerava ispravnost unesenih podataka od strane korisnika pomoću regexa
     *
     * @param provjeraSintakse
     * @return true ako je ispravno ili false ako nije
     */
    private boolean provjeriUnos(String provjeraSintakse) {
        String sintaksaPrikaz = "^-prikaz -s (([a-zA-Z0-9_.-.!.#]{1,})|(([a-zA-Z]:)?(\\\\[a-zA-Z0-9_.-]+)+\\\\?)|((https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]))$";
        String sb = provjeraSintakse;
        String p = sb;
        Pattern pattern = Pattern.compile(sintaksaPrikaz);
        Matcher m = pattern.matcher(p);
        boolean status = m.matches();
        if (status) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Služi za deserijalizaciju podataka iz datoteke
     *
     * @param datotekaEvidencije
     */
    public static void deserijalizirajEvidDatoteku(String datotekaEvidencije) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(datotekaEvidencije);
            ObjectInputStream s = new ObjectInputStream(in);
            Evidencija datoteka = (Evidencija) s.readObject();
            s.close();
            in.close();
            System.out.println("Broj prekinutih zahtjeva: " + datoteka.getBrojPrekinutihZahtjeva());
            System.out.println("Broj uspjesnih zahtjeva: " + datoteka.getBrojUspjesnihZahtjeva());
            System.out.println("Broj ukupnih zahtjeva: " + datoteka.getUkupnoZahtjeva());
            if (datoteka.getZahtjeviZaAdrese() != null) {
                for (Iterator iterator = datoteka.getZahtjeviZaAdrese().iterator(); iterator.hasNext();) {
                    Object next = iterator.next();
                    System.out.println("adrese: " + next);
                }

            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
