/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.ws.serveri;

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
import org.foi.nwtis.fstrahij.PreuzmiPodatkeIzBaze;
import org.foi.nwtis.fstrahij.konfiguracije.Konfiguracija;
import org.foi.nwtis.fstrahij.rest.klijenti.GMKlijent;
import org.foi.nwtis.fstrahij.rest.klijenti.OWMKlijent;
import org.foi.nwtis.fstrahij.web.podaci.Lokacija;
import org.foi.nwtis.fstrahij.web.podaci.MeteoPodaci;
import org.foi.nwtis.fstrahij.web.podaci.Uredjaj;
import org.foi.nwtis.fstrahij.web.slusaci.SlusacAplikacije;

/**
 *
 * @author fstrahij
 */
@WebService(serviceName = "GeoMeteoWS")
public class GeoMeteoWS {

    /**
     * Web service operation Dohvaća sve uređaje iz tablice
     */
    @WebMethod(operationName = "dajSveUredjaje")
    public java.util.List<Uredjaj> dajSveUredjaje() {
        String sql = "SELECT * FROM UREDAJI";

        PreuzmiPodatkeIzBaze podaci = new PreuzmiPodatkeIzBaze();
        ArrayList<Uredjaj> uredjaji = new ArrayList<>(podaci.tablicaUredjajLista(sql));
        return uredjaji;
    }

    /**
     * Web service operation Dodaje novi uređaj u tablicu
     */
    @WebMethod(operationName = "dodajUredjaj")
    public Boolean dodajUredjaj(@WebParam(name = "uredaj") Uredjaj uredaj) {
        //TODO dovršiti upis u bazu podataka  
        int id = 1;
        String naziv = uredaj.getNaziv();
        String adresa = uredaj.getGeoloc().getLatitude();

        GMKlijent gmk = new GMKlijent();
        Lokacija loc = gmk.getGeoLocation(adresa);

        Float latitude = Float.parseFloat(loc.getLatitude());
        Float longitude = Float.parseFloat(loc.getLongitude());

        String sql = "SELECT ID FROM UREDAJI ORDER BY ID DESC";
        PreuzmiPodatkeIzBaze podaci = new PreuzmiPodatkeIzBaze();
        id = podaci.tablicaUredjajInt(sql);

        System.out.println("id uređaj: " + id);
        sql = "INSERT INTO UREDAJI (ID,NAZIV,LATITUDE,LONGITUDE) VALUES (" + id + ",'" + naziv + "'," + latitude + "," + longitude + ")";
        podaci.tablicaUredjajInt(sql);

        return false;
    }

    /**
     * Web service operation Dohvaća sve meteo podatke za odabrani uređaj u
     * intervalu
     */
    @WebMethod(operationName = "dajSveMeteoPodatkeZaUredjaj")
    public List<MeteoPodaci> dajSveMeteoPodatkeZaUredjaj(@WebParam(name = "id") int id, @WebParam(name = "from") long from, @WebParam(name = "to") long to) {
        List<MeteoPodaci> mp = null;
        String od = String.valueOf(from);
        od = od.substring(0, 4) + "-" + od.substring(4, 6) + "-" + od.substring(6, 8);
        String doo = String.valueOf(to);;
        doo = doo.substring(0, 4) + "-" + doo.substring(4, 6) + "-" + doo.substring(6, 8);
        String sql = "SELECT * FROM METEO WHERE ID = " + id + " AND DATE(PREUZETO) BETWEEN '" + od + "' AND '" + doo + "'";
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
        String sql = "SELECT * FROM METEO WHERE ID = " + id + " ORDER BY PREUZETO DESC FETCH FIRST 1 ROWS ONLY";
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
    @WebMethod(operationName = "dajMinMaxTempZaUredjaj")
    public Float[] dajMinMaxTempZaUredjaj(@WebParam(name = "id") int id, @WebParam(name = "from") long from, @WebParam(name = "to") long to) {
        Float[] minMax = new Float[2];
        String od = String.valueOf(from);
        od = od.substring(0, 4) + "-" + od.substring(4, 6) + "-" + od.substring(6, 8);
        String doo = String.valueOf(to);;
        doo = doo.substring(0, 4) + "-" + doo.substring(4, 6) + "-" + doo.substring(6, 8);

        String sql = "SELECT TEMPMIN FROM METEO WHERE ID = " + id + " AND DATE(PREUZETO) \n"
                + "BETWEEN '" + od + "' AND '" + doo + "'\n"
                + "ORDER BY TEMPMIN ASC FETCH FIRST 1 ROWS ONLY";
        PreuzmiPodatkeIzBaze podaci = new PreuzmiPodatkeIzBaze();
        minMax[0] = podaci.tablicaMeteoFloat(sql);

        sql = "SELECT TEMPMAX FROM METEO WHERE ID = " + id + " AND DATE(PREUZETO) \n"
                + "BETWEEN '" + od + "' AND '" + doo + "'\n"
                + "ORDER BY TEMPMAX DESC FETCH FIRST 1 ROWS ONLY";
        minMax[1] = podaci.tablicaMeteoFloat(sql);
        return minMax;
    }

    /**
     * Web service operation Dohvaća minimalnu i maksimalnu vlagu za odabrani
     * uređaj u intervalu
     */
    @WebMethod(operationName = "dajMinMaxVlagaZaUredjaj")
    public Float[] dajMinMaxVlagaZaUredjaj(@WebParam(name = "id") int id, @WebParam(name = "from") long from, @WebParam(name = "to") long to) {
        Float[] minMax = new Float[2];
        String od = String.valueOf(from);
        od = od.substring(0, 4) + "-" + od.substring(4, 6) + "-" + od.substring(6, 8);
        String doo = String.valueOf(to);;
        doo = doo.substring(0, 4) + "-" + doo.substring(4, 6) + "-" + doo.substring(6, 8);

        String sql = "SELECT VLAGA FROM METEO WHERE ID = " + id + " AND DATE(PREUZETO) \n"
                + "BETWEEN '" + od + "' AND '" + doo + "'\n"
                + "ORDER BY VLAGA ASC FETCH FIRST 1 ROWS ONLY";
        PreuzmiPodatkeIzBaze podaci = new PreuzmiPodatkeIzBaze();
        minMax[0] = podaci.tablicaMeteoFloat(sql);

        sql = "SELECT VLAGA FROM METEO WHERE ID = " + id + " AND DATE(PREUZETO) \n"
                + "BETWEEN '" + od + "' AND '" + doo + "'\n"
                + "ORDER BY VLAGA DESC FETCH FIRST 1 ROWS ONLY";
        minMax[1] = podaci.tablicaMeteoFloat(sql);
        return minMax;
    }

    /**
     * Web service operation Dohvaća minimalni i maksimalni tlak za odabrani
     * uređaj u intervalu
     */
    @WebMethod(operationName = "dajMinMaxTlakZaUredjaj")
    public Float[] dajMinMaxTlakZaUredjaj(@WebParam(name = "id") int id, @WebParam(name = "from") long from, @WebParam(name = "to") long to) {
        Float[] minMax = new Float[2];
        from = 20170508;
        to = 20170508;
        String od = String.valueOf(from);
        od = od.substring(0, 4) + "-" + od.substring(4, 6) + "-" + od.substring(6, 8);
        String doo = String.valueOf(to);;
        doo = doo.substring(0, 4) + "-" + doo.substring(4, 6) + "-" + doo.substring(6, 8);

        String sql = "SELECT TLAK FROM METEO WHERE ID = " + id + " AND DATE(PREUZETO) \n"
                + "BETWEEN '" + od + "' AND '" + doo + "'\n"
                + "ORDER BY TLAK ASC FETCH FIRST 1 ROWS ONLY";
        PreuzmiPodatkeIzBaze podaci = new PreuzmiPodatkeIzBaze();
        minMax[0] = podaci.tablicaMeteoFloat(sql);

        sql = "SELECT TLAK FROM METEO WHERE ID = " + id + " AND DATE(PREUZETO) \n"
                + "BETWEEN '" + od + "' AND '" + doo + "'\n"
                + "ORDER BY TLAK DESC FETCH FIRST 1 ROWS ONLY";
        minMax[1] = podaci.tablicaMeteoFloat(sql);
        return minMax;
    }
}
