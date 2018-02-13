/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.rest.serveri;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import javax.ws.rs.core.Response;
import org.foi.nwtis.fstrahij.PreuzmiPodatkeIzBaze;
import org.foi.nwtis.fstrahij.rest.klijenti.GMKlijent;
import org.foi.nwtis.fstrahij.web.podaci.Lokacija;
import org.foi.nwtis.fstrahij.web.podaci.Uredjaj;

/**
 * REST Web Service
 *
 * @author fstrahij
 */
public class MeteoRESTResource {

    private String id;

    /**
     * Creates a new instance of MeteoRESTResource
     */
    private MeteoRESTResource(String id) {
        this.id = id;
    }

    /**
     * Get instance of the MeteoRESTResource
     */
    public static MeteoRESTResource getInstance(String id) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of MeteoRESTResource class.
        return new MeteoRESTResource(id);
    }

    /**
     * Retrieves representation of an instance of
     * org.foi.nwtis.fstrahij.rest.serveri.MeteoRESTResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO dovršiti da se podaci čitaju iz baze meteo

        ArrayList<Uredjaj> uredjaji = new ArrayList<>();
        int i = 0;
        Lokacija geoloc = new Lokacija("0.0", "0.0");
        for (; i < 10; i++) {
            uredjaji.add(new Uredjaj(i, "IoT " + i, geoloc));
        }

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

        return "Nepostoji uređaj sa id: " + this.id;
    }

    /**
     * PUT method for updating or creating an instance of MeteoRESTResource
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
        try {
            content = new String(content.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MeteoRESTResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        JsonReader reader = Json.createReader(new StringReader(content));

        JsonObject jo = reader.readObject();
        String naziv = jo.getString("naziv");
        String adresa = jo.getString("adresa");
        //String lat = jo.getString("lat");
        //String lon = jo.getString("lon");

        System.out.println("Naziv: " + naziv);
        System.out.println("adresa: " + adresa);
        //System.out.println("lon: " + lon);

        int id = 1;

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
    }

    /**
     * DELETE method for resource MeteoRESTResource
     */
    @DELETE
    public void delete() {
    }
}
