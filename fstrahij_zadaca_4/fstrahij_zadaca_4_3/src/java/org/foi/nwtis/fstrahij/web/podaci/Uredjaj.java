/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.nwtis.fstrahij.web.podaci;

import org.foi.nwtis.fstrahij.rest.klijenti.Lokacija;

/**
 *
 * @author fstrahij
 */
public class Uredjaj {
    private int id;
    private String naziv;
    private Lokacija geoloc;

    public Uredjaj() {
    }

    public Uredjaj(int id, String naziv, Lokacija geoloc) {
        this.id = id;
        this.naziv = naziv;
        this.geoloc = geoloc;
    }

    public Lokacija getGeoloc() {
        return geoloc;
    }

    public void setGeoloc(Lokacija geoloc) {
        this.geoloc = geoloc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }        
}
