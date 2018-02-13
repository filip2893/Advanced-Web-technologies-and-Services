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
import org.foi.nwtis.fstrahij.web.podaci.Zahtjev;

/**
 *
 * @author Filip
 */
@ManagedBean
@SessionScoped
public class PregledZahtjeva {
    
    private List<Zahtjev> zahtjevi = new ArrayList<>();
    private int str;
    /**
     * Creates a new instance of PregledZahtjeva
     */
    public PregledZahtjeva() {
    }

    public List<Zahtjev> getZahtjevi() {
        return zahtjevi;
    }

    public int getStr() {
        return str;
    }

    public void setZahtjevi(List<Zahtjev> zahtjevi) {
        this.zahtjevi = zahtjevi;
    }

    public void setStr(int str) {
        this.str = str;
    }
    
    public void dajZahtjeve(){
        String sql = "SELECT * FROM ZAHTJEVI";
        PreuzmiPodatkeIzBaze podaci = new PreuzmiPodatkeIzBaze();
        zahtjevi = podaci.dajZahtjev(sql);
        str = podaci.dajBrojStranicenja(2);
    }
}
