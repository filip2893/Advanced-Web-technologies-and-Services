/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.rest.serveri;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
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
import org.foi.nwtis.fstrahij.baza.PreuzmiPodatkeIzBaze;
import org.foi.nwtis.fstrahij.web.podaci.Uredjaj;

/**
 * REST Web Service
 *
 * @author Filip
 */
@Path("/UredjajREST")
public class UredjajRESTsResourceContainer {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of UredjajRESTsResourceContainer
     */
    public UredjajRESTsResourceContainer() {
    }

    /**
     * Retrieves representation of an instance of
     * org.foi.nwtis.fstrahij.rest.serveri.uredjaj.UredjajRESTsResourceContainer
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        List<Uredjaj> uredjaji = new ArrayList<>();
        String sql = "SELECT * FROM UREDAJI";
        PreuzmiPodatkeIzBaze podaci = new PreuzmiPodatkeIzBaze();
        uredjaji = podaci.dajUredjaj(sql);

        JsonArrayBuilder jab = Json.createArrayBuilder();
        JsonObjectBuilder job = Json.createObjectBuilder();
        for (Uredjaj uredjaj : uredjaji) {
            job.add("uid", uredjaj.getId());
            job.add("naziv", uredjaj.getNaziv());
            job.add("lat", uredjaj.getGeoloc().getLatitude());
            job.add("lon", uredjaj.getGeoloc().getLongitude());
            jab.add(job);
        }
        return jab.build().toString();
    }

    /**
     * POST method for creating an instance of UredjajRESTResource
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
    public UredjajRESTResource getUredjajRESTResource(@PathParam("id") String id) {
        return UredjajRESTResource.getInstance(id);
    }
}
