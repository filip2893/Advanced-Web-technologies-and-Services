/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.rest.serveri;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.fstrahij.baza.PreuzmiPodatkeIzBaze;
import org.foi.nwtis.fstrahij.rest.klijenti.GMKlijent;
import org.foi.nwtis.fstrahij.web.podaci.Lokacija;
import org.foi.nwtis.fstrahij.web.podaci.Uredjaj;

/**
 * REST Web Service
 *
 * @author Filip
 */
public class UredjajRESTResource {

    private String id;

    /**
     * Creates a new instance of UredjajRESTResource
     */
    private UredjajRESTResource(String id) {
        this.id = id;
    }

    /**
     * Get instance of the UredjajRESTResource
     */
    public static UredjajRESTResource getInstance(String id) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of UredjajRESTResource class.
        return new UredjajRESTResource(id);
    }

    /**
     * Retrieves representation of an instance of
     * org.foi.nwtis.fstrahij.rest.serveri.uredjaj.UredjajRESTResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO dovršiti da se podaci čitaju iz baze meteo

        List<Uredjaj> uredjaji = new ArrayList<>();
        String sql = "SELECT * FROM UREDAJI";
        PreuzmiPodatkeIzBaze podaci = new PreuzmiPodatkeIzBaze();
        uredjaji = podaci.dajUredjaj(sql);

        for (Uredjaj uredjaj : uredjaji) {
            if (uredjaj.getId() == Integer.parseInt(this.id)) {
                JsonObjectBuilder job = Json.createObjectBuilder();
                job.add("uid", uredjaj.getId());
                job.add("naziv", uredjaj.getNaziv());
                job.add("lat", uredjaj.getGeoloc().getLatitude());
                job.add("lon", uredjaj.getGeoloc().getLongitude());
                return job.build().toString();
            }
        }
        return null;
    }

    /**
     * PUT method for updating or creating an instance of MeteoRESTResource
     *
     * @param content representation for the resource
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public String putJson(String content) {
        try {
            content = new String(content.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(UredjajRESTResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        JsonReader reader = Json.createReader(new StringReader(content));

        JsonObject jo = reader.readObject();
        String naziv = jo.getString("naziv");
        String adresa = jo.getString("adresa");
        String vrsta = jo.getString("vrsta");

        System.out.println("Naziv: " + naziv);
        System.out.println("adresa: " + adresa);

        int id = 1;

        GMKlijent gmk = new GMKlijent();
        Lokacija loc = gmk.getGeoLocation(adresa);

        Float latitude = Float.parseFloat(loc.getLatitude());
        Float longitude = Float.parseFloat(loc.getLongitude());

        String sql = "SELECT NAZIV FROM UREDAJI";
        PreuzmiPodatkeIzBaze podaci = new PreuzmiPodatkeIzBaze();
        short vrstaZahtjeva;

        if (vrsta.equals("insert")) {
            vrstaZahtjeva = 0;
            boolean postoji = podaci.provjeraPostojanjaUredjaja(sql, naziv, vrstaZahtjeva);
            if (postoji == false) {
                sql = "SELECT ID FROM UREDAJI ORDER BY ID DESC";
                podaci = new PreuzmiPodatkeIzBaze();
                id = podaci.tablicaUredjajInt(sql);

                System.out.println("id uređaj: " + id);
                sql = "INSERT INTO UREDAJI (ID,NAZIV,LATITUDE,LONGITUDE) VALUES (" + id + ",'" + naziv + "'," + latitude + "," + longitude + ")";
                podaci.tablicaUredjajInt(sql);
                return "1";
            }
        } else if (vrsta.equals("update")) {
            vrstaZahtjeva = 1;
            sql = "SELECT ID FROM UREDAJI";
            boolean postoji = podaci.provjeraPostojanjaUredjaja(sql, String.valueOf(this.id), vrstaZahtjeva);
            if (postoji == true) {
                String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                sql = "UPDATE UREDAJI SET NAZIV='" + naziv + "', LATITUDE=" + latitude + ", LONGITUDE=" + longitude + ", vrijeme_promjene='" + timeStamp + "' WHERE ID=" + this.id;
                podaci = new PreuzmiPodatkeIzBaze();
                podaci.tablicaUredjajInt(sql);
                return "1";
                /*
                {"id":1,
                 "naziv":"nekinovi",
                 "adresa":"Croatia, Split",
                 "vrsta":"insert"}
                 */
            }
        }
        return "0";

    }

    /**
     * DELETE method for resource UredjajRESTResource
     */
    @DELETE
    public void delete() {
    }
}
