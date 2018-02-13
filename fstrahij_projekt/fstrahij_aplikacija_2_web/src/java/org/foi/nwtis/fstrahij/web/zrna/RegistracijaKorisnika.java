/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.web.zrna;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.ws.rs.core.Response;
import org.foi.nwtis.fstrahij.ws.klijenti.KorisnikRESTClient;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

/**
 *
 * @author Filip
 */
@Named(value = "registracijaKorisnika")
@SessionScoped
public class RegistracijaKorisnika implements Serializable{

    private String ime, prezime, korime, lozinka, plozinka, email, greske;

    /**
     * Creates a new instance of RegistracijaKorisnika
     */
    public RegistracijaKorisnika() {
    }

    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public String getKorime() {
        return korime;
    }

    public String getLozinka() {
        return lozinka;
    }

    public String getPlozinka() {
        return plozinka;
    }

    public String getEmail() {
        return email;
    }

    public String getGreske() {
        return greske;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public void setKorime(String korime) {
        this.korime = korime;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public void setPlozinka(String plozinka) {
        this.plozinka = plozinka;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGreske(String greske) {
        this.greske = greske;
    }

    public void registracija() {
        if (!korime.isEmpty()
                && !ime.isEmpty()
                && !prezime.isEmpty()
                && !lozinka.isEmpty()
                && !plozinka.isEmpty()
                && !email.isEmpty()) {
            if (!lozinka.equals(plozinka)) {
                greske = "lozinka i ponovljena lozinka ne odgovaraju";
                return;
            }
            String regEmail = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            Pattern pattern = Pattern.compile(regEmail);
            Matcher m = pattern.matcher(email.trim());
            boolean status = m.matches();
            if (!status) {
                greske = "Neispravan unos emaila";
                return;
            }            
            KorisnikRESTClient klijent = new KorisnikRESTClient(korime);
            if (!klijent.getJson().isEmpty()) {
                greske = "Korisničko ime već postoji";
                return;
            }
            greske="";
            String odgovor = klijent.putJson("{\"korime\":\"" + korime + "\""
                    + ",\"lozinka\":\"" + lozinka + "\""
                    + ",\"ime\":\"" + ime + "\""
                    + ",\"prezime\":\"" + prezime + "\""
                    + ",\"email\":\"" + email + "\""
                    + ",\"vrsta\":\"" + "insert" + "\"}");
             if (odgovor.equals("1")) {
                FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "prijava.xhtml");
                  
                greske = "uspjesno izmjenjeno";
            }else{
                greske = "nije uspjesno izmjenjeno";
            }
            /*
                {"id":1,
  "korime":"msin",
   "lozinka":"sin223",
   "ime":"Marko",
   "prezime":"Sin",
   "vrsta":"update"}
                 */
        } else {
            greske = "treba popuniti sva polja";
        }
    }

}
