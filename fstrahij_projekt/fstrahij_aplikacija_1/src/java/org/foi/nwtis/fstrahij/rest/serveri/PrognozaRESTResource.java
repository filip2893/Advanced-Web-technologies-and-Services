/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.rest.serveri;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.fstrahij.baza.PreuzmiPodatkeIzBaze;

/**
 * REST Web Service
 *
 * @author Filip
 */
public class PrognozaRESTResource {

    private String id;

    /**
     * Creates a new instance of PrognozaRESTResource
     */
    private PrognozaRESTResource(String id) {
        this.id = id;
    }

    /**
     * Get instance of the PrognozaRESTResource
     */
    public static PrognozaRESTResource getInstance(String id) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of PrognozaRESTResource class.
        return new PrognozaRESTResource(id);
    }

    /**
     * Retrieves representation of an instance of org.foi.nwtis.fstrahij.rest.serveri.PrognozaRESTResource
     * @return an instance of java.lang.String
     * @throws java.io.UnsupportedEncodingException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() throws UnsupportedEncodingException {
        String sql = "SELECT * FROM UREDAJI WHERE ID = "+id;

        PreuzmiPodatkeIzBaze podaci = new PreuzmiPodatkeIzBaze();
        String adresa = podaci.dajAdresu(sql);
        
        return adresa;
    }

    /**
     * PUT method for updating or creating an instance of PrognozaRESTResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }

    /**
     * DELETE method for resource PrognozaRESTResource
     */
    @DELETE
    public void delete() {
    }
}
