/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.ejb.sb;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import org.foi.nwtis.fstrahij.rest.klijenti.GMKlijent;
import org.foi.nwtis.fstrahij.rest.klijenti.OWMKlijent;
import org.foi.nwtis.fstrahij.web.podaci.Lokacija;
import org.foi.nwtis.fstrahij.web.podaci.MeteoPrognoza;

/**
 * klijent za REST servis
 *
 * @author Filip
 */
@Stateless
@LocalBean
public class MeteoIoTKlijent {

    private String api_key = "";

    /**
     * postavlja apiKey
     *
     * @param apiKey
     */
    public void postaviKorisnickePodatke(String apiKey) {
        this.api_key = apiKey;
    }

    /**
     * vraća lokaciju za adresu
     *
     * @param adresa
     * @return
     */
    public Lokacija dajLokaciju(String adresa) {
        GMKlijent gmk = new GMKlijent();
        Lokacija lok = gmk.getGeoLocation(adresa);
        return lok;
    }

    /**
     * vraća prognozu za adresu i id IoT
     *
     * @param adresa
     * @param id
     * @return
     */
    public MeteoPrognoza[] dajMeteoPrognoze(String adresa, int id) {
        OWMKlijent owmk = new OWMKlijent(api_key);
        System.out.println("apikey:" + api_key);
        Lokacija lok = dajLokaciju(adresa);
        MeteoPrognoza[] mps = owmk.getWeatherForecast(id, lok.getLatitude(), lok.getLongitude());
        return mps;
    }

    /**
     * vraća adresu za geo duzinu i sirinu
     *
     * @param latitude
     * @param longitude
     * @return
     */
    public String dajAdresu(String latitude, String longitude) {
        OWMKlijent owmk = new OWMKlijent(api_key);
        owmk.getRealTimeWeather(latitude, longitude);
        String adresa = owmk.adresa;
        System.out.println("adresa:" + adresa);
        return adresa;
    }

}
