/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.ws.klijenti;

/**
 * Klasa za povezivanje s web servisom
 *
 * @author fstrahij
 */
public class MeteoWSKlijent {

    /**
     * vraća sve uređaje
     *
     * @return
     */

    public static java.util.List<org.foi.nwtis.fstrahij.ws.klijenti.Uredjaj> dajSveUredjaje() {
        org.foi.nwtis.fstrahij.ws.klijenti.GeoMeteoWS_Service service = new org.foi.nwtis.fstrahij.ws.klijenti.GeoMeteoWS_Service();
        org.foi.nwtis.fstrahij.ws.klijenti.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajSveUredjaje();
    }

    /**
     * dodaje novi uređaj
     *
     * @param uredaj
     * @return
     */

    public static Boolean dodajUredjaj(org.foi.nwtis.fstrahij.ws.klijenti.Uredjaj uredaj) {
        org.foi.nwtis.fstrahij.ws.klijenti.GeoMeteoWS_Service service = new org.foi.nwtis.fstrahij.ws.klijenti.GeoMeteoWS_Service();
        org.foi.nwtis.fstrahij.ws.klijenti.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dodajUredjaj(uredaj);
    }

    /**
     * dohvaća meteo podatke za uređaj u intervalu
     *
     * @param id
     * @param from
     * @param to
     * @return
     */

    public static java.util.List<org.foi.nwtis.fstrahij.ws.klijenti.MeteoPodaci> dajSveMeteoPodatkeZaUredjaj(int id, long from, long to) {
        org.foi.nwtis.fstrahij.ws.klijenti.GeoMeteoWS_Service service = new org.foi.nwtis.fstrahij.ws.klijenti.GeoMeteoWS_Service();
        org.foi.nwtis.fstrahij.ws.klijenti.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajSveMeteoPodatkeZaUredjaj(id, from, to);
    }

}
