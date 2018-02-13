/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.web.zrna;

import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import org.foi.nwtis.fstrahij.ws.klijenti.Lokacija;
import org.foi.nwtis.fstrahij.ws.klijenti.MeteoPodaci;
import org.foi.nwtis.fstrahij.ws.klijenti.MeteoRESTClient;
import org.foi.nwtis.fstrahij.ws.klijenti.MeteoWSKlijent;
import org.foi.nwtis.fstrahij.ws.klijenti.Uredjaj;

/**
 * Služi za dodavanje novog uređaja i dohvaćanje podataka za odabrani uređaj u
 * intervalu
 *
 * @author fstrahij
 */
@Named(value = "odabirUredjaja")
@RequestScoped
public class OdabirUredjaja {

    private List<Uredjaj> uredjaji;
    private List<MeteoPodaci> meteo;
    private String id;
    private List<String> proba;
    private String naziv;
    private String adresa;
    private long od;
    private long doo;

    /**
     * Konstruktor
     *
     * Creates a new instance of OdabirUredjaja
     */
    public OdabirUredjaja() {
    }

    /**
     * Getter koji dohvaća vrijednost varijable uredjaji
     *
     * @return
     */
    public List<Uredjaj> getUredjaji() {
        uredjaji = MeteoWSKlijent.dajSveUredjaje();
        return uredjaji;
    }

    /**
     * Setter koji postavlja vrijednost varijable uredjaji
     *
     * @param uredjaji
     */
    public void setUredjaji(List<Uredjaj> uredjaji) {
        this.uredjaji = uredjaji;
    }

    /**
     * Getter koji dohvaća vrijednost varijable Id
     *
     * @return
     */

    public String getId() {
        return id;
    }

    /**
     * Setter koji postavlja vrijednost varijable id
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter koji dohvaća vrijednost varijable naziv
     *
     * @return
     */
    public String getNaziv() {
        return naziv;
    }

    /**
     * Getter koji dohvaća vrijednost varijable adresa
     *
     * @return
     */
    public String getAdresa() {
        return adresa;
    }

    /**
     * Setter koji postavlja vrijednost varijable naziv
     *
     * @param naziv
     */
    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    /**
     * Setter koji postavlja vrijednost varijable adresa
     *
     * @param adresa
     */
    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    /**
     * Getter koji dohvaća vrijednost varijable meteo
     *
     * @return
     */
    public List<MeteoPodaci> getMeteo() {
        return meteo;
    }

    /**
     * Setter koji postavlja vrijednost varijable meteo
     *
     * @param meteo
     */
    public void setMeteo(List<MeteoPodaci> meteo) {
        this.meteo = meteo;
    }

    /**
     * Getter koji dohvaća vrijednost varijable od
     *
     * @return
     */
    public long getOd() {
        return od;
    }

    /**
     * Getter koji dohvaća vrijednost varijable doo
     *
     * @return
     */
    public long getDoo() {
        return doo;
    }

    /**
     * Setter koji postavlja vrijednost varijable od
     *
     * @param od
     */
    public void setOd(long od) {
        this.od = od;
    }

    /**
     * Setter koji postavlja vrijednost varijable doo
     *
     * @param doo
     */
    public void setDoo(long doo) {
        this.doo = doo;
    }

    /**
     * Getter koji dohvaća vrijednost varijable proba
     *
     * @return
     */
    public List<String> getProba() {
        return proba;
    }

    /**
     * Setter koji postavlja vrijednost varijable proba
     *
     * @param proba
     */
    public void setProba(List<String> proba) {
        this.proba = proba;
    }

    /**
     * metoda koja služi za dohvaćanje id-a odabranog uređaja i intervala
     */
    public void dohvatiMeteoSoap() {
        this.meteo = MeteoWSKlijent.dajSveMeteoPodatkeZaUredjaj(Integer.parseInt(proba.get(0)), od, doo);
    }

    /**
     * dodavanje novog uređaja pomoću SOAP-a
     */
    public void dodajNoviUredjaj() {
        Uredjaj uredjaj = new Uredjaj();
        uredjaj.setNaziv(naziv);

        Lokacija loc = new Lokacija();
        loc.setLatitude(adresa);
        uredjaj.setGeoloc(loc);

        MeteoWSKlijent.dodajUredjaj(uredjaj);

    }

    /**
     * dodavanje novog uređaja pomoću REST-a
     */
    public void dodajNoviUredjajRest() {
        uredjaji = getUredjaji();
        int uSizeInt = uredjaji.size();
        uSizeInt++;
        String uSizeString = String.valueOf(uSizeInt);

        MeteoRESTClient klijent = new MeteoRESTClient(uSizeString);
        klijent.putJson("{\"naziv\":\"" + naziv + "\",\"adresa\":\"" + adresa + "\"}");

    }

}
