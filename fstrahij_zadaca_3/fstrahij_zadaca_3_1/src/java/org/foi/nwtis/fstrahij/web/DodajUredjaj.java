/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.foi.nwtis.fstrahij.konfiguracije.Konfiguracija;
import org.foi.nwtis.fstrahij.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.fstrahij.rest.klijenti.GMKlijent;
import org.foi.nwtis.fstrahij.rest.klijenti.OWMKlijent;
import org.foi.nwtis.fstrahij.web.podaci.Lokacija;
import org.foi.nwtis.fstrahij.web.podaci.MeteoPodaci;

/**
 * Klasa koja služi za dodavanje novog uređaja, dohvaćanje geolokacije te
 * dohvaćanje meteo podataka
 *
 * @author fstrahij
 */
@WebServlet(name = "DodajUredjaj", urlPatterns = {"/DodajUredjaj"})
public class DodajUredjaj extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * Dohvaća naziv, adresu i s obzirom o odabranoj akciji poziva metode
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String naziv = "";
        String adresa = "";
        String akcija = "";
        //TODO preuzeti stvarnu akciju

        request.setCharacterEncoding("UTF-8");

        naziv = request.getParameter("naziv");
        adresa = request.getParameter("adresa");
        akcija = request.getParameter("gumb");
        System.out.println("akcija: " + akcija);
        switch (akcija) {
            case "geoLokacija":
                geoLokacija(naziv, adresa, request, response);
                break;
            case "spremi":
                spremi(naziv, adresa, request, response);
                break;
            case "meteoPodaci":
                meteoPodaci(naziv, adresa, request, response);
                break;
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    /**
     * Metoda kojoj se prosljeđu je adresa i naziv, onda se dohvaća geolokacija
     * za tu adresu
     *
     * @param naziv
     * @param adresa
     * @param request
     * @param response
     */
    private void geoLokacija(String naziv, String adresa, HttpServletRequest request, HttpServletResponse response) {

        try {
            System.out.println("Adresa: " + adresa);

            GMKlijent gmk = new GMKlijent();
            Lokacija loc = gmk.getGeoLocation(adresa);
            System.out.println("Latitude: " + loc.getLatitude());
            System.out.println("Longitude: " + loc.getLongitude());

            request.getSession().setAttribute("naziv", naziv);
            request.getSession().setAttribute("adresa", adresa);
            request.getSession().setAttribute("latitude", loc.getLatitude());
            request.getSession().setAttribute("longitude", loc.getLongitude());
            request.getRequestDispatcher("index.jsp").forward(request, response);
            //TODO prikaži podatke u index.jsp
            //TODO zapamti unsenu adresu i geolokaciju
            //TODO riješi problem hrvatskih znakova
        } catch (ServletException | IOException ex) {
            Logger.getLogger(DodajUredjaj.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metoda koja služi za spremanje podataka u tablicu uredjaji
     *
     * @param naziv
     * @param adresa
     * @param request
     * @param response
     */
    private void spremi(String naziv, String adresa, HttpServletRequest request, HttpServletResponse response) {
        try {
            //TODO upiši podatke o uređaju u bazu
            ServletContext sc = getServletContext();
            BP_Konfiguracija bpkonf = (BP_Konfiguracija) sc.getAttribute("BP_Konfig");

            String server = bpkonf.getServerDatabase();
            String baza = server + bpkonf.getUserDatabase();
            String korisnik = bpkonf.getUserUsername();
            String lozinka = bpkonf.getUserPassword();
            String driver = bpkonf.getDriverDatabase();

            int setId = 1;

            Class.forName(driver);
            String sql = "SELECT ID FROM UREDAJI ORDER BY ID DESC";

            try (Connection veza = DriverManager.getConnection(baza, korisnik, lozinka);) {
                Statement naredba = veza.createStatement();
                ResultSet odgovor = naredba.executeQuery(sql);

                if (odgovor.next()) {
                    setId = Integer.parseInt(odgovor.getString("ID"));
                    setId++;
                }
                naziv = request.getSession().getAttribute("naziv").toString();
                Float latitude = Float.parseFloat(request.getSession().getAttribute("latitude").toString());
                Float longitude = Float.parseFloat(request.getSession().getAttribute("longitude").toString());
                sql = "INSERT INTO UREDAJI (ID,NAZIV,LATITUDE,LONGITUDE) VALUES (" + setId + ",'" + naziv + "'," + latitude + "," + longitude + ")";
                naredba = veza.createStatement();
                naredba.executeUpdate(sql);

            } catch (SQLException ex) {
                Logger.getLogger(DodajUredjaj.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DodajUredjaj.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metoda pomoću koje se dohvaćaju meteo podaci i prikazuju u htmlu na
     * zaslon
     *
     * @param naziv
     * @param adresa
     * @param request
     * @param response
     */
    private void meteoPodaci(String naziv, String adresa, HttpServletRequest request, HttpServletResponse response) {
        try {
            ServletContext sc = getServletContext();
            Konfiguracija konf = (Konfiguracija) sc.getAttribute("Konfig");
            String apikey = konf.dajPostavku("apikey");//"da4c3f6d21285a3d67a1117183a99743";
            //TODO pročitaj APIKEY iz konfiguracijske datoteke
            String lat = request.getSession().getAttribute("latitude").toString();
            String lon = request.getSession().getAttribute("longitude").toString();
            //TODO popuni stvarne geo lokacijske podatke za adresu
            OWMKlijent owmk = new OWMKlijent(apikey);
            MeteoPodaci mp = owmk.getRealTimeWeather(lat, lon);
            String temp = mp.getTemperatureValue().toString();
            String vlaga = mp.getHumidityValue().toString();
            String tlak = mp.getPressureValue().toString();
            String sunset = mp.getSunSet().toString();

            request.getSession().setAttribute("temp", temp);
            request.getSession().setAttribute("vlaga", vlaga);
            request.getSession().setAttribute("tlak", tlak);
            request.getSession().setAttribute("sunset", sunset);
            request.getRequestDispatcher("index.jsp").forward(request, response);

            System.out.println("Temperatura: " + temp);
            System.out.println("Vlaga: " + vlaga);
            System.out.println("Tlak: " + tlak);
            System.out.println("Sunset: " + sunset);

        } catch (ServletException | IOException ex) {
            Logger.getLogger(DodajUredjaj.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
