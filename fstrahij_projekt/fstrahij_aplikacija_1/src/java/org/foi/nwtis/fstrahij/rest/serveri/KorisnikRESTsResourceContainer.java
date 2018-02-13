/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.rest.serveri;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
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
import org.foi.nwtis.fstrahij.web.podaci.Korisnik;

/**
 * REST Web Service
 *
 * @author Filip
 */
@Path("/KorisnikREST")
public class KorisnikRESTsResourceContainer {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of KorisnikRESTsResourceContainer
     */
    public KorisnikRESTsResourceContainer() {
    }

    /**
     * Retrieves representation of an instance of
     * org.foi.nwtis.fstrahij.rest.serveri.korisnik.KorisnikRESTsResourceContainer
     *
     * @return an instance of java.lang.String
     * @throws java.net.MalformedURLException
     * @throws java.net.UnknownHostException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() throws MalformedURLException, UnknownHostException {
        long startTime = System.currentTimeMillis();
        List<Korisnik> korisnici = new ArrayList<>();
        String sql = "SELECT * FROM KORISNIK";
        PreuzmiPodatkeIzBaze podaci = new PreuzmiPodatkeIzBaze();
        korisnici = podaci.dajKorisnik(sql);

        URL url = new URL("http://localhost:8080/fstrahij_aplikacija_2_web/faces/pogled_1.xhtml");
        InetAddress address = InetAddress.getByName(url.getHost());
        String temp = address.toString();
        String IP = temp.substring(temp.indexOf("/") + 1, temp.length());

        JsonArrayBuilder jab = Json.createArrayBuilder();
        JsonObjectBuilder job = Json.createObjectBuilder();
        for (Korisnik korisnik : korisnici) {
            job.add("kid", korisnik.getId());
            job.add("korisnicno_ime", korisnik.getKorime());
            job.add("lozinka", korisnik.getLozinka());
            job.add("ime", korisnik.getIme());
            job.add("prezime", korisnik.getPrezime());
            job.add("email", korisnik.getEmail());
            jab.add(job);
        }
        
        String korisnickoIme = context.getAbsolutePath().getRawUserInfo();
        long trajanjeObrade = System.currentTimeMillis();
        trajanjeObrade = trajanjeObrade - startTime;
        int trajanje = (int) trajanjeObrade;

        sql = "INSERT INTO DNEVNIK (korisnik,url,ipadresa,trajanje) values ('" + korisnickoIme + "', '" + url.toString() + "', '" + IP + "', " + trajanje + ")";
        podaci.noviZapisDnevnikRada(sql);

        return jab.build().toString();
    }

    /**
     * POST method for creating an instance of KorisnikRESTResource
     *
     * @param content representation for the new resource
     * @return an HTTP response with content of the created resource
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postJson(String content) {
        //TODO
        return Response.created(context.getAbsolutePath()).build();
    }

    /**
     * Sub-resource locator method for {korisnickoIme}
     */
    @Path("{korisnickoIme}")
    public KorisnikRESTResource getKorisnikRESTResource(@PathParam("korisnickoIme") String korisnickoIme) {
        return KorisnikRESTResource.getInstance(korisnickoIme);
    }
}
