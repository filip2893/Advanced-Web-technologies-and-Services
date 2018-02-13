/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.zadaca_1;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Klasa služi za evidenciju sustava
 *
 * @author Filip
 */
public class Evidencija implements Serializable {

    int ukupnoZahtjeva = 0;
    int brojUspjesnihZahtjeva = 0;
    int brojPrekinutihZahtjeva = 0;
    ArrayList zahtjeviZaAdrese = new ArrayList();

    public int getUkupnoZahtjeva() {
        return ukupnoZahtjeva;
    }

    public int getBrojUspjesnihZahtjeva() {
        return brojUspjesnihZahtjeva;
    }

    public int getBrojPrekinutihZahtjeva() {
        return brojPrekinutihZahtjeva;
    }

    public ArrayList getZahtjeviZaAdrese() {
        return zahtjeviZaAdrese;
    }

    public void setUkupnoZahtjeva(int ukupnoZahtjeva) {
        this.ukupnoZahtjeva = ukupnoZahtjeva;
    }

    public void setBrojUspjesnihZahtjeva(int brojUspjesnihZahtjeva) {
        this.brojUspjesnihZahtjeva = brojUspjesnihZahtjeva;
    }

    public void setBrojPrekinutihZahtjeva(int brojPrekinutihZahtjeva) {
        this.brojPrekinutihZahtjeva = brojPrekinutihZahtjeva;
    }

    public void setZahtjeviZaAdrese(ArrayList zahtjeviZaAdrese) {
        this.zahtjeviZaAdrese = zahtjeviZaAdrese;
    }

}
