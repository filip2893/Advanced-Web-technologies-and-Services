/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.web.podaci;

/**
 *
 * @author Filip
 */
public class Zahtjev {
    private String korime, vrsta, datum;

    public Zahtjev() {
    }

    public Zahtjev(String korime, String vrsta, String datum) {
        this.korime = korime;
        this.vrsta = vrsta;
        this.datum = datum;
    }

    public String getKorime() {
        return korime;
    }

    public String getVrsta() {
        return vrsta;
    }

    public String getDatum() {
        return datum;
    }

    public void setKorime(String korime) {
        this.korime = korime;
    }

    public void setVrsta(String vrsta) {
        this.vrsta = vrsta;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }    
    
}
