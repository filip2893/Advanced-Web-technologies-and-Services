/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij;

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
import javax.servlet.ServletContext;
import org.foi.nwtis.fstrahij.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.fstrahij.web.DodajUredjaj;
import org.foi.nwtis.fstrahij.web.podaci.Lokacija;
import org.foi.nwtis.fstrahij.web.podaci.MeteoPodaci;
import org.foi.nwtis.fstrahij.web.podaci.Uredjaj;
import org.foi.nwtis.fstrahij.web.slusaci.SlusacAplikacije;

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

        server = bpkonf.getServerDatabase();
        baza = server + bpkonf.getUserDatabase();
        korisnik = bpkonf.getUserUsername();
        lozinka = bpkonf.getUserPassword();
        driver = bpkonf.getDriverDatabase();
    }

    /**
     * Metoda kojoj se prosljeđuje sql upit i na temelju kojeg se izvršava upit
     * nad tablicom Uredjaj. Vraća listu uređaja dohvaćenih upitom
     *
     * @param sql
     * @return
     */
    public ArrayList<Uredjaj> tablicaUredjajLista(String sql) {
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
                int id = Integer.parseInt(odgovor.getString("ID"));
                String naziv = odgovor.getString("NAZIV");

                Lokacija geoloc = new Lokacija(lat, lon);
                uredjaji.add(new Uredjaj(id, naziv, geoloc));
            }

        } catch (SQLException ex) {
            Logger.getLogger(DodajUredjaj.class.getName()).log(Level.SEVERE, null, ex);
        }
        return uredjaji;
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
            if (sql.toUpperCase().startsWith("INSERT")) {
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
            Logger.getLogger(DodajUredjaj.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(DodajUredjaj.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (meteo.isEmpty()) {
            return null;
        }
        return meteo;
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
            Logger.getLogger(DodajUredjaj.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (minMax == null) {
            return null;
        }
        return minMax;
    }

}
