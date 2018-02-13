/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.zadaca_1;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.fstrahij.konfiguracije.Konfiguracija;

/**
 * Kreira se konstruktor klase u koji se prenose podaci konfiguracije. Služi za
 * serijalizaciju podataka.
 *
 * @author Filip
 */
public class SerijalizatorEvidencije extends Thread {

    Konfiguracija konf;

    public SerijalizatorEvidencije(Konfiguracija konf) {
        this.konf = konf;
    }

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        int brojZahtjevaZaSerijalizaciju = Integer.parseInt(konf.dajPostavku("brojZahtjevaZaSerijalizaciju"));
        String evidDatoteka = konf.dajPostavku("evidDatoteka");
        System.out.println(this.getClass());
        //TODO dovršite sami

    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }
}
