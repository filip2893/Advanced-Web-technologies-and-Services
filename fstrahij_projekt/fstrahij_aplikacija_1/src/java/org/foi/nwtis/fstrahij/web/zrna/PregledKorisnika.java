/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.web.zrna;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.foi.nwtis.fstrahij.baza.PreuzmiPodatkeIzBaze;
import org.foi.nwtis.fstrahij.web.podaci.Korisnik;

/**
 *
 * @author Filip
 */
@ManagedBean
@SessionScoped
public class PregledKorisnika {
    
    private List<Korisnik> korisnici = new ArrayList<>();
    private int str;
    /**
     * Creates a new instance of PregledKorisnika
     */
    public PregledKorisnika() {
    }

    public List<Korisnik> getKorisnici() {
        return korisnici;
    }

    public void setKorisnici(List<Korisnik> korisnici) {
        this.korisnici = korisnici;
    }

    public int getStr() {
        return str;
    }

    public void setStr(int str) {
        this.str = str;
    }  
    
    public void dajKorisnike(){
        String sql = "SELECT * FROM KORISNIK";
        PreuzmiPodatkeIzBaze podaci = new PreuzmiPodatkeIzBaze();
        korisnici = podaci.dajKorisnik(sql);
        str = podaci.dajBrojStranicenja(0);
    }
    
}
