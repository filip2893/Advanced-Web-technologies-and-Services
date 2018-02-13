/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.baza;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.fstrahij.konfiguracije.Konfiguracija;
import org.foi.nwtis.fstrahij.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.fstrahij.rest.klijenti.OWMKlijent;
import org.foi.nwtis.fstrahij.rest.klijenti.OWMRESTHelper;
import org.foi.nwtis.fstrahij.socket.server.RadnaDretva;
import org.foi.nwtis.fstrahij.web.podaci.Lokacija;
import org.foi.nwtis.fstrahij.web.podaci.MeteoPodaci;
import org.foi.nwtis.fstrahij.web.podaci.Uredjaj;
import org.foi.nwtis.fstrahij.socket.server.SlusacAplikacije;
import org.foi.nwtis.fstrahij.web.podaci.Dnevnik;
import org.foi.nwtis.fstrahij.web.podaci.Korisnik;
import org.foi.nwtis.fstrahij.web.podaci.Zahtjev;

/**
 *
 * Klasa služi za dohvat potrebnih podataka pomoću kojih se spaja na bazu te za
 * isvršavanja upita nad bazom
 *
 * @author Filip
 */
public class PreuzmiPodatkeIzBaze {

    String server;
    String baza;
    String korisnik;
    String lozinka;
    String driver;
    String apikey;
    int sk, sd, sz;

    /**
     * Konstruktor za inicijalizaciju
     */
    public PreuzmiPodatkeIzBaze() {
    }

    /**
     * Metoda koja služi za dohvat atributa iz ServletContexa te preuzimanje
     * potrebnih podataka iz konfiguracijske datoteke
     */
    public void dohvatPodataka() {
        ServletContext sc = SlusacAplikacije.context;

        BP_Konfiguracija bpkonf = (BP_Konfiguracija) sc.getAttribute("BP_Konfig");
        Konfiguracija konf = (Konfiguracija) sc.getAttribute("Konfig");

        server = bpkonf.getServerDatabase();
        baza = server + bpkonf.getUserDatabase();
        korisnik = bpkonf.getUserUsername();
        lozinka = bpkonf.getUserPassword();
        driver = bpkonf.getDriverDatabase();
        apikey = konf.dajPostavku("apikey");
        sk = Integer.parseInt(konf.dajPostavku("stranicenjeKorisnik"));
        sd = Integer.parseInt(konf.dajPostavku("stranicenjeDnevnik"));
        sz = Integer.parseInt(konf.dajPostavku("stranicenjeZahtjev"));
    }

    public int dajBrojStranicenja(int vrsta){
       dohvatPodataka();
        switch(vrsta){
            case 0: return sk;
            case 1: return sd;
            case 2:return sz;
            default: return -1;
        }    
    }
    
    /**
     * Metoda kojoj se prosljeđuje sql upit i na temelju kojeg se izvršava upit
     * nad tablicom Uredjaj. Vraća listu uređaja dohvaćenih upitom
     *
     * @param sql
     * @return
     */
    public void noviZapisDnevnikRada(String sql) {
        dohvatPodataka();
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PreuzmiPodatkeIzBaze.class.getName()).log(Level.SEVERE, null, ex);
        }

        try (Connection veza = DriverManager.getConnection(baza, korisnik, lozinka); Statement naredba = veza.createStatement();) {
            naredba.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(PreuzmiPodatkeIzBaze.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public MeteoPodaci tablicaUredjajLista(String sql) {
        ArrayList<Uredjaj> uredjaji = new ArrayList<>();
        dohvatPodataka();

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PreuzmiPodatkeIzBaze.class.getName()).log(Level.SEVERE, null, ex);
        }

        try (Connection veza = DriverManager.getConnection(baza, korisnik, lozinka); Statement naredba = veza.createStatement(); ResultSet odgovor = naredba.executeQuery(sql);) {

            while (odgovor.next()) {
                String lat = odgovor.getString("LATITUDE");
                String lon = odgovor.getString("LONGITUDE");

                OWMKlijent owmk = new OWMKlijent(apikey);
                MeteoPodaci mp = owmk.getRealTimeWeather(lat, lon);
                return mp;
            }

        } catch (SQLException ex) {
            Logger.getLogger(PreuzmiPodatkeIzBaze.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean provjeraPostojanjaUredjaja(String sql, String nazivIliId, short vrsta) {
        ArrayList<Uredjaj> uredjaji = new ArrayList<>();
        dohvatPodataka();

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PreuzmiPodatkeIzBaze.class.getName()).log(Level.SEVERE, null, ex);
        }

        try (Connection veza = DriverManager.getConnection(baza, korisnik, lozinka); Statement naredba = veza.createStatement(); ResultSet odgovor = naredba.executeQuery(sql);) {
            if (vrsta == 0) {
                while (odgovor.next()) {
                    String naziv2 = odgovor.getString("NAZIV");
                    if (naziv2.equals(nazivIliId)) {
                        return true;
                    }
                }
            } else if (vrsta == 1) {
                while (odgovor.next()) {
                    int id = odgovor.getInt("ID");
                    if (id == Integer.parseInt(nazivIliId)) {
                        return true;
                    }
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(PreuzmiPodatkeIzBaze.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean provjeraPostojanjaKorisnika(String sql, String korime) {
        ArrayList<Uredjaj> uredjaji = new ArrayList<>();
        dohvatPodataka();

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PreuzmiPodatkeIzBaze.class.getName()).log(Level.SEVERE, null, ex);
        }

        try (Connection veza = DriverManager.getConnection(baza, korisnik, lozinka); Statement naredba = veza.createStatement(); ResultSet odgovor = naredba.executeQuery(sql);) {
            
            while (odgovor.next()) {
                String korime2 = odgovor.getString("korIme");
                if (korime2.equals(korime)) {
                    return true;
                }
            }


        } catch (SQLException ex) {
            Logger.getLogger(PreuzmiPodatkeIzBaze.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public String dajAdresu(String sql) throws UnsupportedEncodingException {
        ArrayList<Uredjaj> uredjaji = new ArrayList<>();
        dohvatPodataka();

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PreuzmiPodatkeIzBaze.class.getName()).log(Level.SEVERE, null, ex);
        }

        try (Connection veza = DriverManager.getConnection(baza, korisnik, lozinka); Statement naredba = veza.createStatement(); ResultSet odgovor = naredba.executeQuery(sql);) {

            while (odgovor.next()) {
                String lat = odgovor.getString("LATITUDE");
                String lon = odgovor.getString("LONGITUDE");

                Client client = ClientBuilder.newClient();;
                WebTarget webResource = client.target("http://maps.google.com/maps/api/geocode/")
                        .path("json");
                webResource = webResource.queryParam("latlng", lat + "," + lon);
                String res = webResource.request(MediaType.APPLICATION_JSON).get(String.class);
                JsonReader reader = Json.createReader(new StringReader(res));

                JsonObject jo = reader.readObject();

                String adresa = jo.getJsonArray("results").getJsonObject(1).getString("formatted_address");
                
                return adresa;
            }

        } catch (SQLException ex) {
            Logger.getLogger(PreuzmiPodatkeIzBaze.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Metoda kojoj se prosljeđuje sql upit i na temelju kojeg se izvršava upit
     * nad tablicom Uredjaj. Ako je naredba INSERT onda se unose podaci u
     * tablicu, a ako nije onda se dohvaća posljednji ID i povećava za 1 te se
     * vraća.
     *
     * @param sql
     * @return
     */
    public int tablicaUredjajInt(String sql) {
        dohvatPodataka();

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PreuzmiPodatkeIzBaze.class.getName()).log(Level.SEVERE, null, ex);
        }

        try (Connection veza = DriverManager.getConnection(baza, korisnik, lozinka); Statement naredba = veza.createStatement();) {
            if (sql.toUpperCase().startsWith("INSERT") || sql.toUpperCase().startsWith("UPDATE")) {
                naredba.executeUpdate(sql);
            } else {
                ResultSet odgovor = naredba.executeQuery(sql);
                if (odgovor.next()) {
                    int id = Integer.parseInt(odgovor.getString("ID"));
                    id++;
                    return id;
                } else {
                    return 1;
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(PreuzmiPodatkeIzBaze.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    public int tablicaKorisnikInt(String sql) {
        dohvatPodataka();

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PreuzmiPodatkeIzBaze.class.getName()).log(Level.SEVERE, null, ex);
        }

        try (Connection veza = DriverManager.getConnection(baza, korisnik, lozinka); Statement naredba = veza.createStatement();) {
            if (sql.toUpperCase().startsWith("INSERT") || sql.toUpperCase().startsWith("UPDATE")) {
                naredba.executeUpdate(sql);
            } else {
                ResultSet odgovor = naredba.executeQuery(sql);
                if (odgovor.next()) {
                    int id = Integer.parseInt(odgovor.getString("ID"));
                    id++;
                    return id;
                } else {
                    return 1;
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(PreuzmiPodatkeIzBaze.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     * Metoda kojoj se prosljeđuje sql upit i na temelju kojeg se izvršava upit
     * nad tablicom Meteo. Vraća meteo podatke u listi
     *
     * @param sql
     * @return
     */
    public List<MeteoPodaci> tablicaMeteoLista(String sql) {
        List<MeteoPodaci> meteo = new ArrayList<>();
        dohvatPodataka();

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PreuzmiPodatkeIzBaze.class.getName()).log(Level.SEVERE, null, ex);
        }

        try (Connection veza = DriverManager.getConnection(baza, korisnik, lozinka); Statement naredba = veza.createStatement(); ResultSet odgovor = naredba.executeQuery(sql);) {

            while (odgovor.next()) {
                Float lat = Float.parseFloat(odgovor.getString("LATITUDE"));
                Float lon = Float.parseFloat(odgovor.getString("LONGITUDE"));
                int vrijeme = Integer.parseInt(odgovor.getString("VRIJEME"));
                String vrijemeOpis = odgovor.getString("VRIJEMEOPIS");
                Float temp = Float.parseFloat(odgovor.getString("TEMP"));
                Float tempMin = Float.parseFloat(odgovor.getString("TEMPMIN"));
                Float tempMax = Float.parseFloat(odgovor.getString("TEMPMAX"));
                Float vlaga = Float.parseFloat(odgovor.getString("VLAGA"));
                Float tlak = Float.parseFloat(odgovor.getString("TLAK"));
                Float vjetar = Float.parseFloat(odgovor.getString("VJETAR"));
                Float vjetarSmjer = Float.parseFloat(odgovor.getString("VJETARSMJER"));
                Timestamp preuzeto = Timestamp.valueOf(odgovor.getString("PREUZETO"));

                meteo.add(new MeteoPodaci(new Date(), new Date(),
                        temp, tempMin, tempMax, "C", vlaga, "%", tlak, "hPa",
                        vjetar, "bura", vjetarSmjer, "windDirectionCode", "windDirectionName",
                        1, "cloudsName", "ok", 0.0f, "", "", vrijeme, vrijemeOpis, "weatherIcon", preuzeto));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PreuzmiPodatkeIzBaze.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (meteo.isEmpty()) {
            return null;
        }
        return meteo;
    }

    public List<Uredjaj> dajUredjaj(String sql) {
        dohvatPodataka();
        List<Uredjaj> uredjaji = new ArrayList<>();
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PreuzmiPodatkeIzBaze.class.getName()).log(Level.SEVERE, null, ex);
        }

        try (Connection veza = DriverManager.getConnection(baza, korisnik, lozinka); Statement naredba = veza.createStatement();) {

            ResultSet odgovor = naredba.executeQuery(sql);
            
                while (odgovor.next()) {
                    String lat = odgovor.getString("LATITUDE");
                    String lon = odgovor.getString("LONGITUDE");
                    int id = Integer.parseInt(odgovor.getString("ID"));
                    String naziv = odgovor.getString("NAZIV");

                    Lokacija geoloc = new Lokacija(lat, lon);
                    uredjaji.add(new Uredjaj(id, naziv, geoloc));
                }
            
            return uredjaji;
        } catch (SQLException ex) {
            Logger.getLogger(PreuzmiPodatkeIzBaze.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public List<Korisnik> dajKorisnik(String sql) {
        dohvatPodataka();
        List<Korisnik> korisnici = new ArrayList<>();
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PreuzmiPodatkeIzBaze.class.getName()).log(Level.SEVERE, null, ex);
        }

        try (Connection veza = DriverManager.getConnection(baza, korisnik, lozinka); Statement naredba = veza.createStatement();) {

            ResultSet odgovor = naredba.executeQuery(sql);
            
                while (odgovor.next()) {
                    int id = Integer.parseInt(odgovor.getString("ID"));
                    String korIme = odgovor.getString("korIme");
                    String lozinka = odgovor.getString("lozinka");
                    String ime = odgovor.getString("ime");
                    String prezime = odgovor.getString("prezime");
                    String email = odgovor.getString("email");

                    korisnici.add(new Korisnik(id, korIme, lozinka, ime, prezime, email));
                }
            
            return korisnici;
        } catch (SQLException ex) {
            Logger.getLogger(PreuzmiPodatkeIzBaze.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public List<Dnevnik> dajDnevnik(String sql) {
        dohvatPodataka();
        List<Dnevnik> dnevnici = new ArrayList<>();
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PreuzmiPodatkeIzBaze.class.getName()).log(Level.SEVERE, null, ex);
        }

        try (Connection veza = DriverManager.getConnection(baza, korisnik, lozinka); Statement naredba = veza.createStatement();) {

            ResultSet odgovor = naredba.executeQuery(sql);
            
                while (odgovor.next()) {
                    int id = Integer.parseInt(odgovor.getString("ID"));
                    String korIme = odgovor.getString("korisnik");
                    String url = odgovor.getString("url");
                    String ipadresa = odgovor.getString("ipadresa");
                    String vrijeme = odgovor.getString("vrijeme");
                    int trajanje = Integer.parseInt(odgovor.getString("trajanje"));
                    int status = Integer.parseInt(odgovor.getString("status"));

                    dnevnici.add(new Dnevnik(id, status, korIme, url, ipadresa, vrijeme, trajanje));
                }
            
            return dnevnici;
        } catch (SQLException ex) {
            Logger.getLogger(PreuzmiPodatkeIzBaze.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public List<Zahtjev> dajZahtjev(String sql) {
        dohvatPodataka();
        List<Zahtjev> zahtjevi = new ArrayList<>();
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PreuzmiPodatkeIzBaze.class.getName()).log(Level.SEVERE, null, ex);
        }

        try (Connection veza = DriverManager.getConnection(baza, korisnik, lozinka); Statement naredba = veza.createStatement();) {

            ResultSet odgovor = naredba.executeQuery(sql);
            
                while (odgovor.next()) {
                    String korIme = odgovor.getString("korisnik");
                    String vrsta = odgovor.getString("vrsta");
                    String datum = odgovor.getString("datum");

                    zahtjevi.add(new Zahtjev(korIme,vrsta,datum));
                }
            
            return zahtjevi;
        } catch (SQLException ex) {
            Logger.getLogger(PreuzmiPodatkeIzBaze.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Metoda kojoj se prosljeđuje sql upit i na temelju kojeg se izvršava upit
     * nad tablicom Meteo. Dohvaća najmanje i najveće vrijedosti o tlaku,
     * temperaturi ili vlagi te ih vraća.
     *
     * @param sql
     * @return
     */
    public Float tablicaMeteoFloat(String sql) {
        Float minMax = null;
        dohvatPodataka();
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PreuzmiPodatkeIzBaze.class.getName()).log(Level.SEVERE, null, ex);
        }

        try (Connection veza = DriverManager.getConnection(baza, korisnik, lozinka); Statement naredba = veza.createStatement(); ResultSet odgovor = naredba.executeQuery(sql);) {
            if (sql.toUpperCase().startsWith("SELECT TEMPMIN")) {
                while (odgovor.next()) {
                    minMax = odgovor.getFloat("TEMPMIN");
                }
            } else if (sql.toUpperCase().startsWith("SELECT TEMPMAX")) {
                while (odgovor.next()) {
                    minMax = odgovor.getFloat("TEMPMAX");
                }
            } else if (sql.toUpperCase().startsWith("SELECT VLAGA")) {
                while (odgovor.next()) {
                    minMax = odgovor.getFloat("VLAGA");
                }
            } else if (sql.toUpperCase().startsWith("SELECT TLAK")) {
                while (odgovor.next()) {
                    minMax = odgovor.getFloat("TLAK");
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(PreuzmiPodatkeIzBaze.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (minMax == null) {
            return null;
        }
        return minMax;
    }

}
