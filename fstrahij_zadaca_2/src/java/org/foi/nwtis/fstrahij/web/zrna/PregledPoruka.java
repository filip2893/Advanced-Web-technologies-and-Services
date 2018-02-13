/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.web.zrna;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.fstrahij.konfiguracije.Konfiguracija;
import org.foi.nwtis.fstrahij.web.kontrole.Izbornik;
import org.foi.nwtis.fstrahij.web.kontrole.Poruka;

/**
 * Klasa koja služi da bi se mogle prikazati i pretraživati poruke
 *
 * @author fstrahij
 */
@Named(value = "pregledPoruka")
@RequestScoped
public class PregledPoruka {

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

    FacesContext facesContext = FacesContext.getCurrentInstance();
    HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);

    /**
     * Služi za dohvat podataka i pokretanje sesije Creates a new instance of
     * PregledPoruka
     */
    public PregledPoruka() {
        ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        Konfiguracija konfig = (Konfiguracija) context.getAttribute("Mail_Konfig");

        this.posluzitelj = konfig.dajPostavku("mail.server");
        this.korisnik = konfig.dajPostavku("mail.usernameView");
        this.lozinka = konfig.dajPostavku("mail.passwordView");
        this.brojPrikazanihPoruka = Integer.parseInt(konfig.dajPostavku("mail.numMessages"));
        this.pozicijaDoPoruke = this.brojPrikazanihPoruka;
        try {

            java.util.Properties properties = System.getProperties();
            properties.put("mail.imap.host", this.posluzitelj);
            Session session = Session.getInstance(properties, null);

            store = session.getStore("imap");
            store.connect(this.posluzitelj, this.korisnik, this.lozinka);
            preuzmiMape();
            preuzmiPoruke();
            if (poruke.size() > brojPrikazanihPoruka) {
                poruke.retainAll(poruke.subList(0, this.brojPrikazanihPoruka));
            }
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(PregledPoruka.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(PregledPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * preuzima sve mape
     */
    void preuzmiMape() {
        try {
            //TODO promjeni sa stvarnim preuzimanjem mapa
            Folder[] folders = store.getDefaultFolder().list();
            for (Folder f : folders) {
                mape.add(new Izbornik(f.getName() + "(" + f.getMessageCount() + ")", f.getName()));
            }

        } catch (MessagingException ex) {
            Logger.getLogger(PregledPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Služi za preuzimanje poruka iz određenog foldera
     */
    void preuzmiPoruke() {
        poruke.clear();
        //TODO promjeni sa stvarnim preuzimanjem poruka
        //TODO razmisli o optimiranju preuzimanja poruka
        try {
            Folder folder = store.getFolder(odabranaMapa);
            folder.open(Folder.READ_ONLY);
            Message[] messages = folder.getMessages();

            for (int i = 0; i < messages.length; i++) {
                Date vrijemeSlanja = messages[i].getSentDate();
                Date vrijemeDolaska = messages[i].getReceivedDate();
                String posiljatelj = messages[i].getFrom()[0].toString();
                String predmet = messages[i].getSubject();
                String sadrzaj = messages[i].getContent().toString();
                String vrsta = messages[i].getContentType();

                poruke.add(new Poruka(String.valueOf(i + 1), vrijemeSlanja, vrijemeDolaska, posiljatelj, predmet, sadrzaj, vrsta));
            }
        } catch (MessagingException | IOException ex) {
            Logger.getLogger(PregledPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }

        ukupnoPorukaMapa = poruke.size();
    }

    /**
     * Služi za promjenu mapa poruka
     *
     * @return
     */
    public String promjenaMape() {
        session.setAttribute("do", this.brojPrikazanihPoruka);
        session.setAttribute("Od", 0);
        this.preuzmiPoruke();
        if (poruke.size() > brojPrikazanihPoruka) {
            poruke.retainAll(poruke.subList(0, this.brojPrikazanihPoruka));
        }
        return "promjenaMape";
    }

    /**
     * Služi za pretragu poruka
     *
     * @return
     */
    public String traziPoruke() {
        poruke.clear();
        try {
            Folder folder = store.getFolder(odabranaMapa);
            folder.open(Folder.READ_ONLY);
            Message[] messages = folder.getMessages();
            if (session.getAttribute("od") != null) {
                this.pozicijaOdPoruke = Integer.parseInt(session.getAttribute("od").toString());
            }
            if (session.getAttribute("do") != null) {
                this.pozicijaDoPoruke = Integer.parseInt(session.getAttribute("do").toString());
            }
            if (this.pozicijaDoPoruke > messages.length) {
                this.pozicijaDoPoruke = messages.length;
            }

            for (int i = this.pozicijaOdPoruke; i < this.pozicijaDoPoruke; i++) {
                if (messages[i].getContent().toString().contains(this.traziPoruke)) {
                    Date vrijemeSlanja = messages[i].getSentDate();
                    Date vrijemeDolaska = messages[i].getReceivedDate();
                    String posiljatelj = messages[i].getFrom()[0].toString();
                    String predmet = messages[i].getSubject();
                    String sadrzaj = messages[i].getContent().toString();
                    String vrsta = messages[i].getContentType();
                    poruke.add(new Poruka(String.valueOf(i + 1), vrijemeSlanja, vrijemeDolaska, posiljatelj, predmet, sadrzaj, vrsta));
                }
            }
        } catch (MessagingException | IOException ex) {
            Logger.getLogger(PregledPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "filtirajPoruke";
    }

    /**
     * Dohvaća prethodne poruke klikom na gumb
     *
     * @return
     */
    public String prethodnePoruke() {
        if (session.getAttribute("od") != null) {
            this.pozicijaOdPoruke = Integer.parseInt(session.getAttribute("od").toString());
        }
        this.pozicijaDoPoruke = this.pozicijaOdPoruke;
        this.pozicijaOdPoruke -= this.brojPrikazanihPoruka;
        if (this.pozicijaOdPoruke < 0) {
            this.pozicijaOdPoruke = 0;
            this.pozicijaDoPoruke = this.brojPrikazanihPoruka;
        }
        session.setAttribute("do", this.pozicijaDoPoruke);
        session.setAttribute("od", this.pozicijaOdPoruke);
        this.preuzmiPoruke();
        if (poruke.size() > brojPrikazanihPoruka) {
            poruke.retainAll(poruke.subList(pozicijaOdPoruke, pozicijaDoPoruke));
        }
        return "prethodnePoruke";
    }

    /**
     * Dohvaća sljedeće poruke klikom na gumb
     *
     * @return
     */
    public String sljedecePoruke() {
        if (session.getAttribute("do") != null) {
            this.pozicijaDoPoruke = Integer.parseInt(session.getAttribute("do").toString());
        }

        this.preuzmiPoruke();

        this.pozicijaOdPoruke = this.pozicijaDoPoruke;
        this.pozicijaDoPoruke += brojPrikazanihPoruka;
        if (this.pozicijaDoPoruke > poruke.size()) {
            this.pozicijaDoPoruke = poruke.size();
            this.pozicijaOdPoruke = poruke.size() - brojPrikazanihPoruka;
        }
        session.setAttribute("do", this.pozicijaDoPoruke);
        session.setAttribute("od", this.pozicijaOdPoruke);
        if (poruke.size() > brojPrikazanihPoruka) {
            poruke.retainAll(poruke.subList(pozicijaOdPoruke, pozicijaDoPoruke));
        }
        return "sljedecePoruke";
    }

    /**
     * vraća string promjenaJezika
     *
     * @return
     */
    public String promjenaJezika() {
        return "promjenaJezika";
    }

    /**
     * vraća string saljiPoruku
     *
     * @return
     */
    public String saljiPoruku() {
        return "saljiPoruku";
    }

    /**
     * vraća vrijednost mape
     *
     * @return
     */
    public ArrayList<Izbornik> getMape() {
        return mape;
    }

    /**
     * Vraća vrijednost poruke
     *
     * @return
     */
    public ArrayList<Poruka> getPoruke() {
        return poruke;
    }

    /**
     * Getter koji vraća vrijednost varijable odabranaMapa
     *
     * @return
     */
    public String getOdabranaMapa() {
        return odabranaMapa;
    }

    /**
     * Getter koji vraća vrijednost varijable ukupnoPorukaMapa
     *
     * @return
     */
    public int getUkupnoPorukaMapa() {
        return ukupnoPorukaMapa;
    }

    /**
     * Getter koji vraća vrijednost varijable traziPoruke
     *
     * @return
     */
    public String getTraziPoruke() {
        return traziPoruke;
    }

    /**
     * Setter koji postavlja vrijednost varijable odabranaMapa
     *
     * @param odabranaMapa
     */
    public void setOdabranaMapa(String odabranaMapa) {
        this.odabranaMapa = odabranaMapa;
    }

    /**
     * Setter koji postavlja vrijednost varijable ukupnoPorukaMapa
     *
     * @param ukupnoPorukaMapa
     */
    public void setUkupnoPorukaMapa(int ukupnoPorukaMapa) {
        this.ukupnoPorukaMapa = ukupnoPorukaMapa;
    }

    /**
     * Setter koji postavlja vrijednost varijable traziPoruke
     *
     * @param traziPoruke
     */
    public void setTraziPoruke(String traziPoruke) {
        this.traziPoruke = traziPoruke;
    }

}
