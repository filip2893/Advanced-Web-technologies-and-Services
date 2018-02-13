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
public class Dnevnik {
    private int id, status, trajanje;
    private String korisnik, url, ipadresa, vrijeme;

    public Dnevnik() {
    }

    public Dnevnik(int id, int status, String korisnik, String url, String ipadresa, String vrijeme, int trajanje) {
        this.id = id;
        this.status = status;
        this.korisnik = korisnik;
        this.url = url;
        this.ipadresa = ipadresa;
        this.vrijeme = vrijeme;
        this.trajanje = trajanje;
    }

    public int getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public String getUrl() {
        return url;
    }

    public String getIpadresa() {
        return ipadresa;
    }

    public String getVrijeme() {
        return vrijeme;
    }

    public int getTrajanje() {
        return trajanje;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setIpadresa(String ipadresa) {
        this.ipadresa = ipadresa;
    }

    public void setVrijeme(String vrijeme) {
        this.vrijeme = vrijeme;
    }

    public void setTrajanje(int trajanje) {
        this.trajanje = trajanje;
    }    
    
}
