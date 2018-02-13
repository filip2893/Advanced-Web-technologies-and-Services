/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.web.zrna;

import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.servlet.ServletContext;
import org.foi.nwtis.fstrahij.konfiguracije.Konfiguracija;
import org.foi.nwtis.fstrahij.web.kontrole.Izbornik;
import org.foi.nwtis.fstrahij.web.podaci.Poruka;

/**
 *
 * @author Filip
 */
@Named(value = "mail")
@SessionScoped
public class Mail implements Serializable {

    private String posluzitelj;
    private String korisnik;
    private String lozinka;

    private ArrayList<Izbornik> mape = new ArrayList<>();
    private String odabranaMapa = "INBOX";
    private ArrayList<Poruka> poruke = new ArrayList<>();
    private int ukupnoPorukaMapa = 0;
    private int brojPrikazanihPoruka = 0;
    private int pozicijaOdPoruke = 0;
    private int pozicijaDoPoruke = 0;
    private String traziPoruke;

    private Store store;

    /**
     * Creates a new instance of Mail
     */
    public Mail() throws MessagingException, IOException {
        ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        Konfiguracija konfig = (Konfiguracija) context.getAttribute("Mail_Konfig");

        this.posluzitelj = konfig.dajPostavku("mail.server");
        this.korisnik = konfig.dajPostavku("mail.usernameView");
        this.lozinka = konfig.dajPostavku("mail.passwordView");
        this.brojPrikazanihPoruka = Integer.parseInt(konfig.dajPostavku("mail.numMessages"));
        System.out.println("korisnik: "+ korisnik);

        java.util.Properties properties = System.getProperties();
        properties.put("mail.imap.host", this.posluzitelj);
        Session session = Session.getInstance(properties, null);

        store = session.getStore("imap");
        store.connect(this.posluzitelj, this.korisnik, this.lozinka);
        preuzmiMape();
        preuzmiPoruke();
    }

    /**
     * preuzima sve mape
     */
    void preuzmiMape() throws MessagingException {

        //TODO promjeni sa stvarnim preuzimanjem mapa
        Folder[] folders = store.getDefaultFolder().list();
        for (Folder f : folders) {
            mape.add(new Izbornik(f.getName() + "(" + f.getMessageCount() + ")", f.getName()));
        }

    }

    /**
     * Služi za preuzimanje poruka iz određenog foldera
     */
    void preuzmiPoruke() throws MessagingException, IOException {
        poruke.clear();
        //TODO promjeni sa stvarnim preuzimanjem poruka
        //TODO razmisli o optimiranju preuzimanja poruka

        Folder folder = store.getFolder(odabranaMapa);
        folder.open(Folder.READ_ONLY);
        Message[] messages = folder.getMessages();
        System.out.println("duljina poruka: "+ messages.length);

        for (int i = 0; i < messages.length; i++) {
            Date vrijemeSlanja = messages[i].getSentDate();
            Date vrijemeDolaska = messages[i].getReceivedDate();
            String posiljatelj = messages[i].getFrom()[0].toString();
            String predmet = messages[i].getSubject();
            String sadrzaj = messages[i].getContent().toString();
            String vrsta = messages[i].getContentType();

            poruke.add(new Poruka(String.valueOf(i + 1), vrijemeSlanja, vrijemeDolaska, posiljatelj, predmet, sadrzaj, vrsta));
        }

        ukupnoPorukaMapa = poruke.size();
    }
    public String promjenaMape() throws MessagingException, IOException {
        this.preuzmiPoruke();
        return "promjenaMape";
    }
    

    public String getPosluzitelj() {
        return posluzitelj;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public String getLozinka() {
        return lozinka;
    }

    public ArrayList<Izbornik> getMape() {
        return mape;
    }

    public String getOdabranaMapa() {
        return odabranaMapa;
    }

    public ArrayList<Poruka> getPoruke() {
        return poruke;
    }

    public int getUkupnoPorukaMapa() {
        return ukupnoPorukaMapa;
    }

    public int getBrojPrikazanihPoruka() {
        return brojPrikazanihPoruka;
    }

    public int getPozicijaOdPoruke() {
        return pozicijaOdPoruke;
    }

    public int getPozicijaDoPoruke() {
        return pozicijaDoPoruke;
    }

    public String getTraziPoruke() {
        return traziPoruke;
    }

    public Store getStore() {
        return store;
    }

    public void setPosluzitelj(String posluzitelj) {
        this.posluzitelj = posluzitelj;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public void setMape(ArrayList<Izbornik> mape) {
        this.mape = mape;
    }

    public void setOdabranaMapa(String odabranaMapa) {
        this.odabranaMapa = odabranaMapa;
    }

    public void setPoruke(ArrayList<Poruka> poruke) {
        this.poruke = poruke;
    }

    public void setUkupnoPorukaMapa(int ukupnoPorukaMapa) {
        this.ukupnoPorukaMapa = ukupnoPorukaMapa;
    }

    public void setBrojPrikazanihPoruka(int brojPrikazanihPoruka) {
        this.brojPrikazanihPoruka = brojPrikazanihPoruka;
    }

    public void setPozicijaOdPoruke(int pozicijaOdPoruke) {
        this.pozicijaOdPoruke = pozicijaOdPoruke;
    }

    public void setPozicijaDoPoruke(int pozicijaDoPoruke) {
        this.pozicijaDoPoruke = pozicijaDoPoruke;
    }

    public void setTraziPoruke(String traziPoruke) {
        this.traziPoruke = traziPoruke;
    }

    public void setStore(Store store) {
        this.store = store;
    }
    
    

}
