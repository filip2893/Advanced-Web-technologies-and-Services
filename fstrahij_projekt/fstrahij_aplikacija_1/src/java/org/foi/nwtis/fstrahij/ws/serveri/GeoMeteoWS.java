/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.ws.serveri;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.servlet.ServletContext;
import org.foi.nwtis.fstrahij.baza.PreuzmiPodatkeIzBaze;
import org.foi.nwtis.fstrahij.konfiguracije.Konfiguracija;
import org.foi.nwtis.fstrahij.rest.klijenti.GMKlijent;
import org.foi.nwtis.fstrahij.rest.klijenti.OWMKlijent;
import org.foi.nwtis.fstrahij.web.podaci.Lokacija;
import org.foi.nwtis.fstrahij.web.podaci.MeteoPodaci;
import org.foi.nwtis.fstrahij.web.podaci.Uredjaj;
import org.foi.nwtis.fstrahij.socket.server.SlusacAplikacije;

/**
 *
 * @author fstrahij
 */
@WebService(serviceName = "GeoMeteoWS")
public class GeoMeteoWS {

    /**
     * Web service operation Dohvaća sve uređaje iz tablice
     */
    @WebMethod(operationName = "dajMeteoPodatakZaUredjaj")
    public List<MeteoPodaci> dajMeteoPodatakZaUredjaj(@WebParam(name = "id") int id) {
        List<MeteoPodaci> mp = null;
        String sql = "SELECT * FROM METEO WHERE ID = "+id+" LIMIT 1";

        PreuzmiPodatkeIzBaze podaci = new PreuzmiPodatkeIzBaze();
        if (podaci.tablicaMeteoLista(sql) != null) {
            mp = new ArrayList<>(podaci.tablicaMeteoLista(sql));
        }
        return mp;
    }

    /**
     * Web service operation Dohvaća zadnje unešene meteo podatke za odabrani
     * uređaj
     */
    @WebMethod(operationName = "dajZadnjeMeteoPodatkeZaUredjaj")
    public List<MeteoPodaci> dajZadnjeMeteoPodatkeZaUredjaj(@WebParam(name = "id") int id) {
        //vraća posljednje spremljene meteo podatake iz baze podatka za uneseni IoT uređaj, ukoliko nema podataka vraća null
        List<MeteoPodaci> mp = null;
        String sql = "SELECT * FROM METEO WHERE ID = " + id + " ORDER BY PREUZETO DESC LIMIT 1";
        PreuzmiPodatkeIzBaze podaci = new PreuzmiPodatkeIzBaze();
        if (podaci.tablicaMeteoLista(sql) != null) {
            mp = new ArrayList<>(podaci.tablicaMeteoLista(sql));
        }
        return mp;
    }

    /**
     * Web service operation Dohvaća zadnje unešene meteo podatke za odabrani
     * uređaj
     */
    @WebMethod(operationName = "nZadnjihMeteoPodatkaZaUredjaj")
    public List<MeteoPodaci> nZadnjihMeteoPodatkaZaUredjaj(@WebParam(name = "id") int id, @WebParam(name = "n") int n) {
        //vraća posljednje spremljene meteo podatake iz baze podatka za uneseni IoT uređaj, ukoliko nema podataka vraća null
        List<MeteoPodaci> mp = null;
        String sql = "SELECT * FROM METEO WHERE ID = " + id + " ORDER BY ID DESC LIMIT " + n;
        PreuzmiPodatkeIzBaze podaci = new PreuzmiPodatkeIzBaze();
        if (podaci.tablicaMeteoLista(sql) != null) {
            mp = new ArrayList<>(podaci.tablicaMeteoLista(sql));
        }
        return mp;
    }

    /**
     * Web service operation Dohvaća minimalnu i maksimalnu temperaturu za
     * odabrani uređaj u intervalu
     */
    @WebMethod(operationName = "dajMeteoPodatkeZaIoTUIntervalu")
    public List<MeteoPodaci> dajMeteoPodatkeZaIoTUIntervalu(@WebParam(name = "id") int id, @WebParam(name = "from") long from, @WebParam(name = "to") long to) {
        List<MeteoPodaci> mp = null;
        String od = String.valueOf(from);
        od = od.substring(0, 4) + "-" + od.substring(4, 6) + "-" + od.substring(6, 8);
        String doo = String.valueOf(to);;
        doo = doo.substring(0, 4) + "-" + doo.substring(4, 6) + "-" + doo.substring(6, 8);

        String sql = "SELECT * FROM METEO WHERE ID = " + id + " AND DATE(PREUZETO) \n"
                + "BETWEEN '" + od + "' AND '" + doo+"'";
        PreuzmiPodatkeIzBaze podaci = new PreuzmiPodatkeIzBaze();
        if (podaci.tablicaMeteoLista(sql) != null) {
            mp = new ArrayList<>(podaci.tablicaMeteoLista(sql));
        }
        return mp;
    }
    
    /**
     * Web service operation Dohvaća sve uređaje iz tablice
     */
    @WebMethod(operationName = "dajAdresuZaUredjaj")
    public String dajAdresuZaUredjaj(@WebParam(name = "id") int id) throws UnsupportedEncodingException {
        String sql = "SELECT * FROM UREDAJI WHERE ID = "+id;

        PreuzmiPodatkeIzBaze podaci = new PreuzmiPodatkeIzBaze();
        String adresa = podaci.dajAdresu(sql);
        
        return adresa;
    }
}
