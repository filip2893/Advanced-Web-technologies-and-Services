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
 * slučaj kada nema slobodne radne dretve kako bi se moglo javiti korisniku.
 *
 * @author Filip
 */
public class RezervnaDretva extends Thread {

    Konfiguracija konf;

    public RezervnaDretva(Konfiguracija konf) {
        this.konf = konf;
    }

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        int trajanjeSpavanja = Integer.parseInt(konf.dajPostavku("intervalAdresneDretve"));

        while (true) {
            System.out.println(this.getClass());
            long trenutnoVrijeme = System.currentTimeMillis();
            //TODO dovršite sami
            long vrijemeZavrsetka = System.currentTimeMillis();

            try {
                sleep(trajanjeSpavanja - (vrijemeZavrsetka - trenutnoVrijeme));
            } catch (InterruptedException ex) {
                Logger.getLogger(RezervnaDretva.class.getName()).log(Level.SEVERE, null, ex);
            }

            //TODO razmisliti kako izaći iz beskonačne petlje
            //TODO razmisliti kako izaći iz beskonačne petlje
            //TODO razmisliti kako izaći iz beskonačne petlje
            //TODO razmisliti kako izaći iz beskonačne petlje
        }
    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }
}
