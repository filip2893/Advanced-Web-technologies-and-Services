/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.socket.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.fstrahij.konfiguracije.Konfiguracija;
import org.foi.nwtis.fstrahij.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.fstrahij.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.fstrahij.konfiguracije.NemaKonfiguracije;

/**
 * Prvo se provjeravaju upisane opcije, koristeći dopuštene izraze. Učitavaju se
 * postavke iz datoteke. Server kreira i pokreće nadzornu dretvu (klasa
 * NadzorDretvi), kreira i pokreće adresnu dretvu (klasa ProvjeraAdresa), kreira
 * i pokreće rezervnu dretvu (klasa RezervnaDretva), kreira i pokreće dretvu za
 * serijalizaciju evidencije (klasa SerijalizatorEvidencije). Otvara se
 * ServerSocket na izabranom portu i čeka zahtjev korisnika u beskonačnoj
 * petlji. Kada se korisnik spoji na otvorenu vezu, kreira se objekt dretve
 * klase RadnaDretva, veza se predaje objektu i pokreće se izvršavanje dretve.
 * Dretve opslužuju zahtjev korisnika. Dretva nakon što obradi pridruženi
 * zahtjev korisnika završava svoj rad i briše se.
 *
 * @author Filip
 */
public class ServerSustava extends Thread {

    private ServerSocket serverSocket;

    public ServerSustava(int port) {
        try {
            serverSocket = new ServerSocket(8000);
        } catch (IOException ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.

    }

    @Override
    public void run() {
        while (true) {
            try {
                if (serverSocket != null) {
                    Socket socket = serverSocket.accept();

                    //TODO dodaj dretvu u kolekciju aktivnih radnih dretvi
                    RadnaDretva rd = new RadnaDretva(socket);

                    rd.start();
                }
            } catch (IOException ex) {
                Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * kreira i pokreće nadzornu dretvu (klasa NadzorDretvi), kreira i pokreće
     * adresnu dretvu (klasa ProvjeraAdresa), kreira i pokreće rezervnu dretvu
     * (klasa RezervnaDretva), kreira i pokreće dretvu za serijalizaciju
     * evidencije (klasa SerijalizatorEvidencije). Otvara se ServerSocket na
     * izabranom portu i čeka zahtjev korisnika u beskonačnoj petlji. Kada se
     * korisnik spoji na otvorenu vezu, kreira se objekt dretve klase
     * RadnaDretva, veza se predaje objektu i pokreće se izvršavanje dretve.
     * Dretve opslužuju zahtjev korisnika.
     *
     * @param nazivDatoteke
     * @param trebaUcitatiEvidenciju
     */
    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }
}
