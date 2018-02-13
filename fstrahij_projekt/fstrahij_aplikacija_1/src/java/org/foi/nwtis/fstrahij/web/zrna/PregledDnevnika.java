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
import org.foi.nwtis.fstrahij.web.podaci.Dnevnik;

/**
 *
 * @author Filip
 */
@ManagedBean
@SessionScoped
public class PregledDnevnika {
    
    private List<Dnevnik> dnevnik = new ArrayList<>();
    private int str;
    
    /**
     * Creates a new instance of PregledDnevnika
     */
    public PregledDnevnika() {
    }

    public List<Dnevnik> getDnevnik() {
        return dnevnik;
    }

    public int getStr() {
        return str;
    }

    public void setDnevnik(List<Dnevnik> dnevnik) {
        this.dnevnik = dnevnik;
    }

    public void setStr(int str) {
        this.str = str;
    }
    
    public void dajDnevnik(){
        String sql = "SELECT * FROM DNEVNIK";
        PreuzmiPodatkeIzBaze podaci = new PreuzmiPodatkeIzBaze();
        dnevnik = podaci.dajDnevnik(sql);
        str = podaci.dajBrojStranicenja(1);
    }
}
