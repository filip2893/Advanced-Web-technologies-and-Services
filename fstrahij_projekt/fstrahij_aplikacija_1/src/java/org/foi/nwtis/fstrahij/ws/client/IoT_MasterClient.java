/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.ws.client;

import java.net.URISyntaxException;
import javax.jws.WebService;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;
import org.foi.nwtis.fstrahij.socket.server.IoTMaster;

/**
 *
 * @author Filip
 */
@WebService(serviceName = "IoT_Master", portName = "IoT_MasterPort", endpointInterface = "org.foi.nwtis.dkermek.ws.serveri.IoTMaster", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", wsdlLocation = "WEB-INF/wsdl/IoT_MasterClient/nwtis.foi.hr_8080/DZ3_Master/IoT_Master.wsdl")
public class IoT_MasterClient {

    public static java.lang.Boolean obrisiSveUredjajeGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.dkermek.ws.serveri.IoTMaster port = IoTMaster.service.getIoTMasterPort();
        return port.obrisiSveUredjajeGrupe(korisnickoIme, korisnickaLozinka);
    }

    public static boolean ucitajSveUredjajeGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.dkermek.ws.serveri.IoTMaster port = IoTMaster.service.getIoTMasterPort();
        return port.ucitajSveUredjajeGrupe(korisnickoIme, korisnickaLozinka);
    }

    public static org.foi.nwtis.dkermek.ws.serveri.StatusUredjaja dajStatusUredjajaGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, int idUredjaj) {
        org.foi.nwtis.dkermek.ws.serveri.IoTMaster port = IoTMaster.service.getIoTMasterPort();
        return port.dajStatusUredjajaGrupe(korisnickoIme, korisnickaLozinka, idUredjaj);
    }

    public static boolean blokirajUredjajGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, int idUredjaj) {
        org.foi.nwtis.dkermek.ws.serveri.IoTMaster port = IoTMaster.service.getIoTMasterPort();
        return port.blokirajUredjajGrupe(korisnickoIme, korisnickaLozinka, idUredjaj);
    }

    public static boolean aktivirajUredjajGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, int idUredjaj) {
        org.foi.nwtis.dkermek.ws.serveri.IoTMaster port = IoTMaster.service.getIoTMasterPort();
        return port.aktivirajUredjajGrupe(korisnickoIme, korisnickaLozinka, idUredjaj);
    }

    public static boolean obrisiUredjajGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, int idUredjaj) {
        org.foi.nwtis.dkermek.ws.serveri.IoTMaster port = IoTMaster.service.getIoTMasterPort();
        return port.obrisiUredjajGrupe(korisnickoIme, korisnickaLozinka, idUredjaj);
    }

    public boolean aktivirajOdabraneUredjajeGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, java.util.List<java.lang.Integer> odabraniUredjaji) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean blokirajOdabraneUredjajeGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, java.util.List<java.lang.Integer> odabraniUredjaji) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean obrisiOdabraneUredjajeGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, java.util.List<java.lang.Integer> odabraniUredjaji) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public java.lang.Boolean autenticirajGrupuIoT(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) throws URISyntaxException {
      
        return true;
    }

    public static java.lang.Boolean aktivirajGrupuIoT(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.dkermek.ws.serveri.IoTMaster port = IoTMaster.service.getIoTMasterPort();
        return port.aktivirajGrupuIoT(korisnickoIme, korisnickaLozinka);
    }

    public static org.foi.nwtis.dkermek.ws.serveri.StatusKorisnika dajStatusGrupeIoT(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.dkermek.ws.serveri.IoTMaster port = IoTMaster.service.getIoTMasterPort();
        return port.dajStatusGrupeIoT(korisnickoIme, korisnickaLozinka);
    }

    public static java.lang.Boolean dodajUredjajGrupi(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, org.foi.nwtis.dkermek.ws.serveri.Uredjaj iotUredjaj) {
        //TODO implement this method
        org.foi.nwtis.dkermek.ws.serveri.IoTMaster port = IoTMaster.service.getIoTMasterPort();
        return port.dodajUredjajGrupi(korisnickoIme, korisnickaLozinka, iotUredjaj);
    }

    public java.lang.Boolean dodajNoviUredjajGrupi(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, int idUredjaj, java.lang.String nazivUredjaj, java.lang.String adresaUredjaj) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public static java.util.List<org.foi.nwtis.dkermek.ws.serveri.Uredjaj> dajSveUredjajeGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.dkermek.ws.serveri.IoTMaster port = IoTMaster.service.getIoTMasterPort();
        return port.dajSveUredjajeGrupe(korisnickoIme, korisnickaLozinka);
    }

    public static java.lang.Boolean registrirajGrupuIoT(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.dkermek.ws.serveri.IoTMaster port = IoTMaster.service.getIoTMasterPort();
        return port.registrirajGrupuIoT(korisnickoIme, korisnickaLozinka);
    }

    public static java.lang.Boolean deregistrirajGrupuIoT(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.dkermek.ws.serveri.IoTMaster port = IoTMaster.service.getIoTMasterPort();
        return port.deregistrirajGrupuIoT(korisnickoIme, korisnickaLozinka);
    }

    public static java.lang.Boolean blokirajGrupuIoT(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.dkermek.ws.serveri.IoTMaster port = IoTMaster.service.getIoTMasterPort();
        return port.blokirajGrupuIoT(korisnickoIme, korisnickaLozinka);
    }
    
}
