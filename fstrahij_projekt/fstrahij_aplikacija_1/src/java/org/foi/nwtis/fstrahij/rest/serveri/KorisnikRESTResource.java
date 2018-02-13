/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.rest.serveri;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
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
import org.foi.nwtis.fstrahij.web.podaci.Korisnik;
import org.foi.nwtis.fstrahij.web.podaci.Lokacija;
import org.foi.nwtis.fstrahij.web.podaci.Uredjaj;

/**
 * REST Web Service
 *
 * @author Filip
 */
public class KorisnikRESTResource {

    private String korisnickoIme;

    /**
     * Creates a new instance of KorisnikRESTResource
     */
    private KorisnikRESTResource(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    /**
     * Get instance of the KorisnikRESTResource
     */
    public static KorisnikRESTResource getInstance(String korisnickoIme) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of KorisnikRESTResource class.
        return new KorisnikRESTResource(korisnickoIme);
    }

    /**
     * Retrieves representation of an instance of
     * org.foi.nwtis.fstrahij.rest.serveri.korisnici.KorisnikRESTResource
     *
     * @return an instance of java.lang.String
     * @throws java.net.MalformedURLException
     * @throws java.net.UnknownHostException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() throws MalformedURLException, UnknownHostException {
        //TODO return proper representation object
        long startTime = System.currentTimeMillis();
        List<Korisnik> korisnici = new ArrayList<>();
        String sql = "SELECT * FROM KORISNIK";
        PreuzmiPodatkeIzBaze podaci = new PreuzmiPodatkeIzBaze();
        korisnici = podaci.dajKorisnik(sql);

        URL url = new URL("http://localhost:8080/fstrahij_aplikacija_2_web/faces/prijava.xhtml");
        InetAddress address = InetAddress.getByName(url.getHost());
        String temp = address.toString();
        String IP = temp.substring(temp.indexOf("/") + 1, temp.length());

        for (Korisnik korisnik : korisnici) {
            if (korisnik.getKorime().equals(this.korisnickoIme)) {
                JsonObjectBuilder job = Json.createObjectBuilder();
                job.add("kid", korisnik.getId());
                job.add("korisnicno_ime", korisnik.getKorime());
                job.add("lozinka", korisnik.getLozinka());
                job.add("ime", korisnik.getIme());
                job.add("prezime", korisnik.getPrezime());
                job.add("email", korisnik.getEmail());

                long trajanjeObrade = System.currentTimeMillis();
                trajanjeObrade = trajanjeObrade - startTime;
                int trajanje = (int) trajanjeObrade;

                sql = "INSERT INTO DNEVNIK (korisnik,url,ipadresa,trajanje) values ('" + korisnickoIme + "', '" + url.toString() + "', '" + IP + "', " + trajanje + ")";
                podaci.noviZapisDnevnikRada(sql);

                return job.build().toString();
            }
        }

        return null;
    }

    /**
     * PUT method for updating or creating an instance of KorisnikRESTResource
     *
     * @param content representation for the resource
     * @return
     * @throws java.net.MalformedURLException
     * @throws java.net.UnknownHostException
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public String putJson(String content) throws MalformedURLException, UnknownHostException {
        long startTime = System.currentTimeMillis();
        try {
            content = new String(content.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(UredjajRESTResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        JsonReader reader = Json.createReader(new StringReader(content));

        JsonObject jo = reader.readObject();
        String lozinka = jo.getString("lozinka");
        String korime = jo.getString("korime");
        String ime = jo.getString("ime");
        String prezime = jo.getString("prezime");
        String email = jo.getString("email");
        String vrsta = jo.getString("vrsta");

        String sql = "SELECT korIme, lozinka FROM KORISNIK";
        PreuzmiPodatkeIzBaze podaci = new PreuzmiPodatkeIzBaze();

        if (vrsta.equals("insert")) {
            URL url = new URL("http://localhost:8080/fstrahij_aplikacija_2_web/faces/registracija.xhtml");
            InetAddress address = InetAddress.getByName(url.getHost());
            String temp = address.toString();
            String IP = temp.substring(temp.indexOf("/") + 1, temp.length());

            boolean postoji = podaci.provjeraPostojanjaKorisnika(sql, korime);
            if (postoji == false) {

                sql = "INSERT INTO Korisnik (korIme,lozinka,ime, prezime, email) VALUES ('" + korime + "','" + lozinka + "','" + ime + "','" + prezime + "','" + email + "')";
                podaci.tablicaKorisnikInt(sql);

                long trajanjeObrade = System.currentTimeMillis();
                trajanjeObrade = trajanjeObrade - startTime;
                int trajanje = (int) trajanjeObrade;

                sql = "INSERT INTO DNEVNIK (korisnik,url,ipadresa,trajanje) values ('" + korisnickoIme + "', '" + url.toString() + "', '" + IP + "', " + trajanje + ")";
                podaci.noviZapisDnevnikRada(sql);
                return "1";
            }
        } else if (vrsta.equals("update")) {
            URL url = new URL("http://localhost:8080/fstrahij_aplikacija_2_web/faces/pogled_1.xhtml");
            InetAddress address = InetAddress.getByName(url.getHost());
            String temp = address.toString();
            String IP = temp.substring(temp.indexOf("/") + 1, temp.length());

            boolean postoji = podaci.provjeraPostojanjaKorisnika(sql, korime);
            if (postoji == true) {

                String korimeNovo = jo.getString("korimeNovo");
                sql = "UPDATE KORISNIK SET korIme='" + korimeNovo + "', lozinka='" + lozinka + "', ime='" + ime + "', prezime='" + prezime + "', email='" + email + "' WHERE korIme='" + korime + "'";
                podaci = new PreuzmiPodatkeIzBaze();
                podaci.tablicaKorisnikInt(sql);

                long trajanjeObrade = System.currentTimeMillis();
                trajanjeObrade = trajanjeObrade - startTime;
                int trajanje = (int) trajanjeObrade;

                sql = "INSERT INTO DNEVNIK (korisnik,url,ipadresa,trajanje) values ('" + korisnickoIme + "', '" + url.toString() + "', '" + IP + "', " + trajanje + ")";
                podaci.noviZapisDnevnikRada(sql);

                return "1";
                /*
                {"id":1,
  "korime":"msin",
   "lozinka":"sin223",
   "ime":"Marko",
   "prezime":"Sin",
   "vrsta":"update"}
                 */
            }
        }
        return "0";
    }

    /**
     * DELETE method for resource KorisnikRESTResource
     */
    @DELETE
    public void delete() {
    }
}
