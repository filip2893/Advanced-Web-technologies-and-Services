/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.socket.server;

import java.net.URISyntaxException;
import java.util.List;
import org.foi.nwtis.dkermek.ws.serveri.StatusKorisnika;
import org.foi.nwtis.dkermek.ws.serveri.StatusUredjaja;
import org.foi.nwtis.fstrahij.rest.klijenti.GMKlijent;
import org.foi.nwtis.fstrahij.web.podaci.Lokacija;
import org.foi.nwtis.fstrahij.web.podaci.Uredjaj;
import org.foi.nwtis.fstrahij.ws.client.IoT_MasterClient;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.Listener;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

/**
 *
 * @author Filip
 */
public class IoT {

    public static org.foi.nwtis.dkermek.ws.serveri.IoTMaster_Service service = IoTMaster.service;

    public static CallbackConnection connection;
    private String user = "fstrahij";
    private String password = "ITaMU";

    public int Add(int id, String naziv, String adresa) throws URISyntaxException, InterruptedException {
        org.foi.nwtis.dkermek.ws.serveri.Uredjaj iotUredjaj = new org.foi.nwtis.dkermek.ws.serveri.Uredjaj();
        iotUredjaj.setId(id);
        iotUredjaj.setNaziv(naziv);

        GMKlijent gmk = new GMKlijent();
        Lokacija locPros = gmk.getGeoLocation("Croatia, Split");

        org.foi.nwtis.dkermek.ws.serveri.Lokacija loc = new org.foi.nwtis.dkermek.ws.serveri.Lokacija();
        loc.setLatitude(locPros.getLatitude());
        loc.setLongitude(locPros.getLongitude());
        iotUredjaj.setGeoloc(loc);
        iotUredjaj.setStatus(StatusUredjaja.AKTIVAN);

        if (IoT_MasterClient.dodajUredjajGrupi(user, password, iotUredjaj)) {
            return 0;
        } else {
            return 20;
        }
    }

    public int Work(int id) {
        if (IoT_MasterClient.aktivirajUredjajGrupe(user, password, id)) {
            return 0;
        } else {
            return 31;
        }

    }

    public int Wait(int id) {
        if (IoT_MasterClient.blokirajUredjajGrupe(user, password, id)) {
            return 0;
        } else {
            return 32;
        }

    }

    public int Remove(int id) throws URISyntaxException, InterruptedException {
        if (IoT_MasterClient.obrisiUredjajGrupe(user, password, id)) {
            return 0;
        } else {
            return 33;
        }
    }

    public int StatusUredjaja(int id) {
        StatusUredjaja status = IoT_MasterClient.dajStatusUredjajaGrupe(user, password, id);
        if (status.value().equals("AKTIVAN")) {
            return 25;
        } 
        return 24;
    }
}
