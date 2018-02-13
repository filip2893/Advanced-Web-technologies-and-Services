/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.rest.serveri;

import java.io.StringReader;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.foi.nwtis.fstrahij.PreuzmiPodatkeIzBaze;
import org.foi.nwtis.fstrahij.web.podaci.Lokacija;
import org.foi.nwtis.fstrahij.web.podaci.Uredjaj;

/**
 * REST Web Service
 *
 * @author fstrahij
 */
@Path("/meteoREST")
public class MeteoRESTResourceContainer {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of MeteoRESTResourceContainer
     */
    public MeteoRESTResourceContainer() {
    }

    /**
     * Retrieves representation of an instance of
     * org.foi.nwtis.fstrahij.rest.serveri.MeteoRESTResourceContainer
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO dovršiti da se podaci čitaju iz baze
        String sql = "SELECT * FROM UREDAJI";

        PreuzmiPodatkeIzBaze podaci = new PreuzmiPodatkeIzBaze();
        ArrayList<Uredjaj> uredjaji = new ArrayList<>(podaci.tablicaUredjajLista(sql));

        JsonArrayBuilder jab = Json.createArrayBuilder();
        for (Uredjaj uredjaj : uredjaji) {
            JsonObjectBuilder job = Json.createObjectBuilder();
            job.add("uid", uredjaj.getId());
            job.add("naziv", uredjaj.getNaziv());
            job.add("lat", uredjaj.getGeoloc().getLatitude());
            job.add("lon", uredjaj.getGeoloc().getLongitude());

            jab.add(job);
        }

        return jab.build().toString();
    }

    /**
     * POST method for creating an instance of MeteoRESTResource
     *
     * @param content representation for the new resource
     * @return an HTTP response with content of the created resource
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postJson(String content) {

        JsonReader reader = Json.createReader(new StringReader(content));

        JsonObject jo = reader.readObject();
        int uid = jo.getInt("uid");
        String naziv = jo.getString("naziv");
        String lat = jo.getString("lat");
        String lon = jo.getString("lon");

        System.out.println("UID: " + uid);
        System.out.println("Naziv: " + naziv);
        System.out.println("lat: " + lat);
        System.out.println("lon: " + lon);

        //TODO upiši uređaj u bazu
        return Response.created(context.getAbsolutePath()).build();
    }

    /**
     * Sub-resource locator method for {id}
     */
    @Path("{id}")
    public MeteoRESTResource getMeteoRESTResource(@PathParam("id") String id) {
        return MeteoRESTResource.getInstance(id);
    }
}
