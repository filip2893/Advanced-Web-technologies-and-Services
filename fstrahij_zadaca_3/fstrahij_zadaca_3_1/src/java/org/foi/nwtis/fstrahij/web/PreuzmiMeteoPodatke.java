/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.foi.nwtis.fstrahij.konfiguracije.Konfiguracija;
import org.foi.nwtis.fstrahij.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.fstrahij.rest.klijenti.OWMKlijent;
import org.foi.nwtis.fstrahij.web.podaci.MeteoPodaci;

/**
 * Dretva za spremljene IoT uređaji odnosno njihove geo lokacije preuzima
 * meterološke podatke i sprema u odgovarajuću tablicu pod nazivom METEO
 *
 * @author Filip
 */
public class PreuzmiMeteoPodatke extends Thread {

    private ServletContext sc = null;
    private boolean stop = false;

    @Override
    public void interrupt() {
        stop = true;
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        BP_Konfiguracija bpkonf = (BP_Konfiguracija) sc.getAttribute("BP_Konfig");
        Konfiguracija konf = (Konfiguracija) sc.getAttribute("Konfig");

        String server = bpkonf.getServerDatabase();
        String baza = server + bpkonf.getUserDatabase();
        String korisnik = bpkonf.getUserUsername();
        String lozinka = bpkonf.getUserPassword();
        String driver = bpkonf.getDriverDatabase();
        int trajanjeCiklusa = Integer.parseInt(konf.dajPostavku("intervalDretveZaMeteoPodatke"));
        String apikey = konf.dajPostavku("apikey");

        while (!stop) {
            try {

                System.out.println("Dretva je pokrenuta!");

                try {
                    Class.forName(driver);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(PreuzmiMeteoPodatke.class.getName()).log(Level.SEVERE, null, ex);
                }
                String sql = "SELECT * FROM UREDAJI";

                try (Connection veza = DriverManager.getConnection(baza, korisnik, lozinka);) {
                    Statement naredba = veza.createStatement();
                    ResultSet odgovor = naredba.executeQuery(sql);

                    while (odgovor.next()) {
                        int id = Integer.parseInt(odgovor.getString("ID"));
                        String lat = odgovor.getString("LATITUDE");
                        String lon = odgovor.getString("LONGITUDE");

                        OWMKlijent owmk = new OWMKlijent(apikey);
                        MeteoPodaci mp = owmk.getRealTimeWeather(lat, lon);
                        Float temp = Float.parseFloat(mp.getTemperatureValue().toString());
                        Float tempMin = Float.parseFloat(mp.getTemperatureMin().toString());
                        Float tempMax = Float.parseFloat(mp.getTemperatureMax().toString());
                        Float vlaga = Float.parseFloat(mp.getHumidityValue().toString());
                        Float tlak = Float.parseFloat(mp.getPressureValue().toString());
                        Float vjetar = Float.parseFloat(mp.getWindSpeedValue().toString());
                        Float vjetarSmjer = Float.parseFloat(mp.getWindDirectionValue().toString());
                        String vrijeme = String.valueOf(mp.getWeatherNumber());
                        String vrijemeOpis = mp.getWeatherValue();

                        sql = "INSERT INTO METEO (id,adresaStanice,latitude,longitude,vrijeme,"
                                + "vrijemeOpis,temp,tempMin,tempMax,vlaga,tlak,vjetar,vjetarSmjer) "
                                + "VALUES (" + id + ",'fstrahij'," + lat + "," + lon + ",'" + vrijeme + "','" + vrijemeOpis + "',"
                                + "" + temp + "," + tempMin + "," + tempMax + "," + vlaga + "," + tlak + "," + vjetar + "," + vjetarSmjer + ")";
                        naredba = veza.createStatement();
                        naredba.executeUpdate(sql);
                    }
                    odgovor.close();
                    naredba.close();
                    veza.close();

                } catch (SQLException ex) {
                    Logger.getLogger(DodajUredjaj.class.getName()).log(Level.SEVERE, null, ex);
                }

                sleep(trajanjeCiklusa * 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(PreuzmiMeteoPodatke.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Setter koji postavlja služi za postavljanje vrijednosti ServletContexta
     *
     * @param sc
     */
    public void setSc(ServletContext sc) {
        this.sc = sc;
    }

}
