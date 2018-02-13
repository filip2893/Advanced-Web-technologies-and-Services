/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.web.app3.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.fstrahij.ejb.sb.AutentifikacijaKorisnika;

/**
 *
 * @author Filip
 */
@Named(value = "prijava")
@SessionScoped
public class prijava implements Serializable {

    @EJB
    private AutentifikacijaKorisnika autentifikacijaKorisnika;

    
    
     private String korime, lozinka, greske;

    /**
     * Creates a new instance of prijava
     */
    public prijava() {
    }

    public String getKorime() {
        return korime;
    }

    public String getLozinka() {
        return lozinka;
    }

    public String getGreske() {
        return greske;
    }

    public void setKorime(String korime) {
        this.korime = korime;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public void setGreske(String greske) {
        this.greske = greske;
    }
    
    
    
    public void prijava() {
        if (!korime.isEmpty() && !lozinka.isEmpty()) {
            int ak = autentifikacijaKorisnika.prijavaKorisnika(korime, lozinka);
            switch (ak) {
                case 0:
                    FacesContext facesContext = FacesContext.getCurrentInstance();
                    HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
                    session.setAttribute("korime", korime);
                    session.setAttribute("lozinka", lozinka);
                    greske = "uspjesna prijava";
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
