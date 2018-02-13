/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.socket.server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.foi.nwtis.fstrahij.konfiguracije.Konfiguracija;
import org.foi.nwtis.fstrahij.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.fstrahij.rest.klijenti.OWMKlijent;
import org.foi.nwtis.fstrahij.web.podaci.MeteoPodaci;

/**
 * Klasa služi za obradu poruka tj. dretvi. Dretva se starta prilikom
 * inicijaliziranja konteksta i traje određeni ciklus. Sve poruke se obrađuju te
 * se prema obradi kopiraju u odgovaraju foldere te u bazu podataka. Provode se
 * i statističke obrade koje se šalju na određenu mail adrese koja je određena
 * konfiguracijom
 *
 * @author fstrahij
 */
public class PreuzmiMeteoPodatke extends Thread {

    public static boolean pause = false;
    private ServletContext sc = null;
    public boolean stop = false;
    private HashMap<String, MeteoPodaci> metOpt = new HashMap<>();

    /**
     * Služi za prekid rada dretve
     */
    @Override
    public void interrupt() {
        stop = true;
        super.interrupt();
    }

    /**
     * metoda run pokreće dretvu koja obrađuje poruke
     *
     */
    @Override
    public void run() {
        Konfiguracija konf = (Konfiguracija) sc.getAttribute("Konfig");
        int trajanjeObrade = Integer.parseInt(konf.dajPostavku("intervalDretveZaMeteoPodatke"));
        String apikey = konf.dajPostavku("apikey");
        int port = Integer.parseInt(konf.dajPostavku("port"));

        BP_Konfiguracija bpkonf = (BP_Konfiguracija) sc.getAttribute("BP_Konfig");
        String serverDB = bpkonf.getServerDatabase();
        String baza = serverDB + bpkonf.getUserDatabase();
        String korisnikDB = bpkonf.getUserUsername();
        String lozinkaDB = bpkonf.getUserPassword();
        String driver = bpkonf.getDriverDatabase();
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PreuzmiMeteoPodatke.class.getName()).log(Level.SEVERE, null, ex);
        }

        int redniBrojCiklusa = 0;
        synchronized (this) {
            while (!stop) {
                redniBrojCiklusa++;
                System.out.println("ciklus: " + redniBrojCiklusa);

                while (pause) {
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(PreuzmiMeteoPodatke.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                try (Connection veza = DriverManager.getConnection(baza, korisnikDB, lozinkaDB);) {

                String sql = "SELECT * FROM uredaji";
                Statement naredba = veza.createStatement();
                ResultSet odgovor = naredba.executeQuery(sql);
                MeteoPodaci mp =  new MeteoPodaci();

                while (odgovor.next()) {
                    int id = Integer.parseInt(odgovor.getString("ID"));
                    String lat = odgovor.getString("LATITUDE");
                    String lon = odgovor.getString("LONGITUDE");
                    String key = lat + lon;
                    
                    if (!metOpt.isEmpty() && metOpt.containsKey(key)) {
                                mp = metOpt.get(key); 
                    } else {
                        OWMKlijent owmk = new OWMKlijent(apikey);
                        mp = owmk.getRealTimeWeather(lat, lon);
                        metOpt.put(key, mp);
                        
                    }
                    Float temp = Float.parseFloat(mp.getTemperatureValue().toString());
                    Float tempMin = Float.parseFloat(mp.getTemperatureMin().toString());
                    Float tempMax = Float.parseFloat(mp.getTemperatureMax().toString());
                    Float vlaga = Float.parseFloat(mp.getHumidityValue().toString());
                    Float tlak = Float.parseFloat(mp.getPressureValue().toString());
                    Float vjetar = Float.parseFloat(mp.getWindSpeedValue().toString());
                    Float vjetarSmjer = Float.parseFloat(mp.getWindDirectionValue().toString());
                    String vrijeme = String.valueOf(mp.getWeatherNumber());
                    String vrijemeOpis = mp.getWeatherValue();                    

                    sql = "INSERT INTO meteo (id,adresaStanice,latitude,longitude,vrijeme,"
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
                Logger.getLogger(PreuzmiMeteoPodatke.class.getName()).log(Level.SEVERE, null, ex);
            }
                try {
                    sleep(trajanjeObrade * 1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(PreuzmiMeteoPodatke.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (stop) {
                interrupt();
            }
        }
    }

    public synchronized void pokreniCekanje() {
        pause = false;
        notify();
    }
    
    public synchronized void zaustaviDretvu() {
        pokreniCekanje();
        stop = true;
        notify();
    }

    @Override
    public State getState() {
        return super.getState(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isInterrupted() {
        return super.isInterrupted(); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Pokreće dretvu
     */
    @Override
    public synchronized void start() {
        super.start();
    }

    /**
     * Služi za startanje dretve
     *
     * @param sc
     */
    public void setSc(ServletContext sc) {
        this.sc = sc;
    }

}
