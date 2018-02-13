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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import org.foi.nwtis.fstrahij.ejb.eb.Dnevnik;
import org.foi.nwtis.fstrahij.ejb.sb.DnevnikFacade;

/**
 * Zrno koje služi za obavljanje operacija nad pogledom pregledDnevnika.xml
 *
 * @author Filip
 */
@Named(value = "pregledDnevnika")
@SessionScoped
public class PregledDnevnika implements Serializable {

    @EJB
    private DnevnikFacade dnevnikFacade;

    private String ipadresa, korisnik, status, trajanje;
    private List<Dnevnik> dnevnik = new ArrayList<>();

    /**
     * Creates a new instance of PregledDnevnika
     */
    public PregledDnevnika() {
    }

    /**
     * Getter koji dohvaća vrijednost varijable ipadresa
     *
     * @return
     */
    public String getIpadresa() {
        return ipadresa;
    }

    /**
     * Getter koji dohvaća vrijednost varijable korisnik
     *
     * @return
     */
    public String getKorisnik() {
        return korisnik;
    }

    /**
     * Getter koji dohvaća vrijednost varijable status
     *
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     * Getter koji dohvaća vrijednost varijable trajanje
     *
     * @return
     */
    public String getTrajanje() {
        return trajanje;
    }

    /**
     * Getter koji dohvaća vrijednost liste dnevnik
     *
     * @return
     */
    public List<Dnevnik> getDnevnik() {
        return dnevnik;
    }

    /**
     * Setter koji postavlja vrijednost varijable ipadresa
     *
     * @param ipadresa
     */
    public void setIpadresa(String ipadresa) {
        this.ipadresa = ipadresa;
    }

    /**
     * Setter koji postavlja vrijednost varijable korisnik
     *
     * @param korisnik
     */
    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    /**
     * Setter koji postavlja vrijednost varijable status
     *
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Setter koji postavlja vrijednost varijable trajanje
     *
     * @param trajanje
     */
    public void setTrajanje(String trajanje) {
        this.trajanje = trajanje;
    }

    /**
     * Setter koji postavlja vrijednost varijable dnevnik
     *
     * @param dnevnik
     */
    public void setDnevnik(List<Dnevnik> dnevnik) {
        this.dnevnik = dnevnik;
    }

    /**
     * služi za dohvat podataka o dnevniku u listu
     */
    public void preuzmiPodatkeIzDnevnika() {
        List<Dnevnik> d = dnevnikFacade.findAll();
        dnevnik.clear();
        for (Dnevnik dn : d) {
            dnevnik.add(new Dnevnik(dn.getId(),
                    dn.getKorisnik(),
                    dn.getUrl(),
                    dn.getIpadresa(),
                    dn.getTrajanje(),
                    dn.getStatus()));

        }
        if (!korisnik.isEmpty()) {
            dnevnik = dnevnik.stream()
                    .filter(s -> s.getKorisnik().equals(korisnik))
                    .collect(Collectors.toList());
        }
        if (!ipadresa.isEmpty()) {
            dnevnik = dnevnik.stream()
                    .filter(s -> s.getIpadresa().equals(ipadresa))
                    .collect(Collectors.toList());
        }
        if (!trajanje.isEmpty()) {
            int tra = Integer.parseInt(status);
            dnevnik = dnevnik.stream()
                    .filter(s -> s.getTrajanje() == tra)
                    .collect(Collectors.toList());
        }
        if (!status.isEmpty()) {
            int stat = Integer.parseInt(status);
            dnevnik = dnevnik.stream()
                    .filter(s -> s.getStatus() == stat)
                    .collect(Collectors.toList());
        }
    }

}
