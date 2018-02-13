/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.socket.server;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.foi.nwtis.dkermek.ws.serveri.StatusKorisnika;
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
public class IoTMaster {
    public static org.foi.nwtis.dkermek.ws.serveri.IoTMaster_Service service = new org.foi.nwtis.dkermek.ws.serveri.IoTMaster_Service();
        
    public static CallbackConnection connection;
    private String user = "fstrahij";
    private String password = "ITaMU";
    
    public int Start() throws URISyntaxException, InterruptedException {
        if (IoT_MasterClient.registrirajGrupuIoT(user, password)) {
             return 0;
        }else{
            return 20;
        }
    }

    public int Stop() {
        if (IoT_MasterClient.deregistrirajGrupuIoT(user, password)) {
             return 0;
        }else{
        
        }
        return 21;
    }
    
     public int Work() {
         if (IoT_MasterClient.aktivirajGrupuIoT(user, password)) {
             return 0;
         }else{
            return 22;
         }
        
    }
     
     public int Wait() {
         if (IoT_MasterClient.blokirajGrupuIoT(user, password)) {
             return 0;
         }else{
            return 23;
         }
        
    }
     
     public int StatusKorisnika() {
         StatusKorisnika  status =  IoT_MasterClient.dajStatusGrupeIoT(user, password);
         if (status.value().equals("BLOKIRAN")) {
             return 24;
         }else{
            return 25;
         }        
    }
     
     public int Load() {
        if (IoT_MasterClient.ucitajSveUredjajeGrupe(user, password)) {
             return 0;
        }else{
        
        }
        return 21;
    }
    
     public int Clear() {
         if (IoT_MasterClient.obrisiSveUredjajeGrupe(user, password)) {
             return 0;
         }else{
            return 22;
         }
        
    }
     
     public String List() {
         java.util.List<org.foi.nwtis.dkermek.ws.serveri.Uredjaj> uredjaji = IoT_MasterClient.dajSveUredjajeGrupe(user, password);
         String uredjajJson = null;
         if (uredjaji.isEmpty()) {
             return uredjajJson;
         }
 
         uredjajJson = "[";
         
         uredjajJson = uredjaji.stream().map((uredjaj) -> "{\"naziv\":\""+uredjaj.getNaziv()+"\", \"id\":\""+uredjaj.getId()+"\"},").reduce(uredjajJson, String::concat);
         
         int kraj = uredjajJson.length()-1;
         uredjajJson = uredjajJson.substring(0, kraj);
         uredjajJson += "]"; 
         System.out.println("lista: "+uredjajJson);
         /*String odgovor = klijent.putJson("\"naziv\":\"" + azurirajNaziv + "\""
                    + ",\"adresa\":\"" + azurirajAdresa + "\""
                    + ",\"vrsta\":\"" + "update" + "\"}");
         for (org.foi.nwtis.dkermek.ws.serveri.Uredjaj uredjaj :uredjaji) {
             uredjajJson += "{\"naziv\":\""+uredjaj.getNaziv()+"\", \"id\":\""+uredjaj.getId()+"\"},";
         }
         */
         return uredjajJson;
    }
}