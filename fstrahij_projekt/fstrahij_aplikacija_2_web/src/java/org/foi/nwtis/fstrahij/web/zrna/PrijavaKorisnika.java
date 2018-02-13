/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.web.zrna;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.json.JsonObject;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.fstrahij.ejb.sb.AutentikacijaKorisnika;
import org.foi.nwtis.fstrahij.ws.klijenti.KorisnikRESTClient;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

/**
 *
 * @author Filip
 */
@Named(value = "prijavaKorisnika")
@SessionScoped
public class PrijavaKorisnika implements Serializable {

    @EJB
    private AutentikacijaKorisnika autentikacijaKorisnika;

    private String korime, lozinka, greske;
    private boolean pregledOstalih = false;    
    private boolean prijavaKorisnika = true;

    /**
     * Creates a new instance of PrijavaKorisnika
     */
    public PrijavaKorisnika() {
    }

    public String getKorime() {
        return korime;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setKorime(String korime) {
        this.korime = korime;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getGreske() {
        return greske;
    }

    public void setGreske(String greske) {
        this.greske = greske;
    }

    public boolean isPregledOstalih() {
        return pregledOstalih;
    }

    public void setPregledOstalih(boolean pregledOstalih) {
        this.pregledOstalih = pregledOstalih;
    }

    public boolean isPrijavaKorisnika() {
        return prijavaKorisnika;
    }

    public void setPrijavaKorisnika(boolean prijavaKorisnika) {
        this.prijavaKorisnika = prijavaKorisnika;
    }

    public void prijava() {
        if (!korime.isEmpty() && !lozinka.isEmpty()) {
            int ak = autentikacijaKorisnika.prijavaKorisnika(korime, lozinka);
            switch (ak) {
                case 0:
                    FacesContext facesContext = FacesContext.getCurrentInstance();
                    HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
                    session.setAttribute("korime", korime);
                    session.setAttribute("lozinka", lozinka);                    
                    greske = "uspjesna prijava";
                    prijavaKorisnika = false;
                    pregledOstalih = true;
                    FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "index.xhtml");
                    break;
                case 1:
                    greske = "neispravno korisničko ime";
                    break;
                case 2:
                    greske = "neispravna lozinka";
                    break;
            }
        } else {
            greske = "nisu uneseni svi podaci";
        }
    }
}
