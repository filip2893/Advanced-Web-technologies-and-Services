/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.zadaca_1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Klasa služi za dohvat unesnih korisničkih podataka. Dohvaćeni podaci se
 * provjeravaju pomoću regexa i nakon toga se šalju serveru.
 *
 * @author Filip
 */
public class AdministratorSustava extends VezaSustava {

    /**
     * Dohvaća potrebne podatke za uspostavu konekcije na određenom portu sa
     * serverom
     *
     * @param nazivServera
     * @param port
     * @param korIme
     * @param lozinka
     * @param komanda
     */
    public void pokreniAdministratora(String nazivServera, int port, String korIme, String lozinka, String komanda) {
        String provjeraSintakse = "-admin -server " + nazivServera + " -port " + port + " -u " + korIme + " -p " + lozinka + " -" + komanda.toLowerCase();
        String zahtjev = "USER " + korIme + "; PASSWD " + lozinka + "; " + komanda + ";";
        if (provjeriUnos(provjeraSintakse)) {
            pokreniKorisnika(nazivServera, port, zahtjev);
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
        String ipv4 = "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
        String sintaksaAdmin = "^-admin -server (" + ipv4 + "|([a-zA-Z0-9_.-]{1,})) -port ([0-9]{4})? -u ([a-zA-Z0-9_.-]{1,}) -p ([a-zA-Z0-9_.-.!.#]{1,})( -pause| -start| -stop| -stat)?$";
        String sb = provjeraSintakse;
        String p = sb;
        Pattern pattern = Pattern.compile(sintaksaAdmin);
        Matcher m = pattern.matcher(p);
        boolean status = m.matches();
        if (status) {
            return true;
        } else {
            return false;
        }
    }
}
