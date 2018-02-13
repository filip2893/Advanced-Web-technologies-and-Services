/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import org.foi.nwtis.fstrahij.web.kontrole.Izbornik;

/**
 * Klasa koja služi za postavljanje odabranog jezika u aplikaciji
 *
 * @author fstrahij
 */
@Named(value = "lokalizator")
@SessionScoped
public class Lokalizacija implements Serializable {

    private static final ArrayList<Izbornik> izbornikJezika = new ArrayList<>();
    private String odabraniJezik;

    static {
        izbornikJezika.add(new Izbornik("hrvatski", "hr"));
        izbornikJezika.add(new Izbornik("engleski", "en"));
        izbornikJezika.add(new Izbornik("njemački", "de"));
    }

    /**
     * Creates a new instance of Lokalizacija
     */
    public Lokalizacija() {
    }

    /**
     * Getter koji vraća izbornikJezika
     *
     * @return
     */
    public ArrayList<Izbornik> getIzbornikJezika() {
        return izbornikJezika;
    }

    /**
     * Getter koji dohvaća koji je jezik korisnik odabrao te vraća vrijednost
     * varijable odabraniJezik
     *
     * @return
     */
    public String getOdabraniJezik() {
        //FacesContext FC = FacesContext.getCurrentInstance();
        UIViewRoot UIVR = FacesContext.getCurrentInstance().getViewRoot();
        if (UIVR != null) {
            Locale lokalniJezik = FacesContext.getCurrentInstance().getViewRoot().getLocale();
            odabraniJezik = lokalniJezik.getLanguage();
        }
        return odabraniJezik;
    }

    /**
     *
     * @param odabraniJezik
     */
    public void setOdabraniJezik(String odabraniJezik) {
        this.odabraniJezik = odabraniJezik;
        Locale lokalniJezik = new Locale(odabraniJezik);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(lokalniJezik);
    }

    /**
     * Postavlja odabrani jezik
     *
     * @return
     */
    public Object odaberiJezik() {
        setOdabraniJezik(odabraniJezik);
        return "PromjenaJezika";
    }

    /**
     * Služi za slanje poruka
     *
     * @return
     */
    public Object saljiPoruku() {
        return "saljiPoruku";
    }

    /**
     * Služi za pregled poruka
     *
     * @return
     */
    public Object pregledPoruka() {
        return "pregledPoruka";
    }
}
