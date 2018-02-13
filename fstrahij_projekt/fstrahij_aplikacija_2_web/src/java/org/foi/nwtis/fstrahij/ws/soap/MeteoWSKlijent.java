/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.ws.soap;

import org.foi.nwtis.fstrahij.ws.serveri.MeteoPodaci;

/**
 *
 * @author Filip
 */
public class MeteoWSKlijent {

    public static String dajAdresuZaUredjaj(int id) {
        org.foi.nwtis.fstrahij.ws.serveri.GeoMeteoWS_Service service = new org.foi.nwtis.fstrahij.ws.serveri.GeoMeteoWS_Service();
        org.foi.nwtis.fstrahij.ws.serveri.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajAdresuZaUredjaj(id);
    }

    public static java.util.List<org.foi.nwtis.fstrahij.ws.serveri.MeteoPodaci> dajZadnjeMeteoPodatkeZaUredjaj(int id) {
        org.foi.nwtis.fstrahij.ws.serveri.GeoMeteoWS_Service service = new org.foi.nwtis.fstrahij.ws.serveri.GeoMeteoWS_Service();
        org.foi.nwtis.fstrahij.ws.serveri.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajZadnjeMeteoPodatkeZaUredjaj(id);
    }

    public static java.util.List<org.foi.nwtis.fstrahij.ws.serveri.MeteoPodaci> dajMeteoPodatakZaUredjaj(int id) {
        org.foi.nwtis.fstrahij.ws.serveri.GeoMeteoWS_Service service = new org.foi.nwtis.fstrahij.ws.serveri.GeoMeteoWS_Service();
        org.foi.nwtis.fstrahij.ws.serveri.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajMeteoPodatakZaUredjaj(id);
    }

       
    
}
