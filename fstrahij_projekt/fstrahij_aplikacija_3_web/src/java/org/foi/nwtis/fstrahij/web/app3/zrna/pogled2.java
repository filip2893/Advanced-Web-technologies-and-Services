/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.web.app3.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.fstrahij.server.app3.socket.VezaSustava;

/**
 *
 * @author Filip
 */
@Named(value = "pogled2")
@SessionScoped
public class pogled2 implements Serializable {
    private String odgovor="", korime, lozinka;
    /**
     * Creates a new instance of pogled2
     */
    public pogled2() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        korime = session.getAttribute("korime").toString();
        lozinka = session.getAttribute("lozinka").toString();
    }
    
    public void IoT_MasterStart(){
        VezaSustava vs = new VezaSustava();
        vs.posaljiNaSocketu("localhost", 8000, "USER " + korime + "; PASSWD " + lozinka + "; IoT_Master START;");
        odgovor=VezaSustava.odgovor;
    }
    
    public void IoT_MasterStop(){
        VezaSustava vs = new VezaSustava();
        vs.posaljiNaSocketu("localhost", 8000, "USER " + korime + "; PASSWD " + lozinka + "; IoT_Master STOP;");
        odgovor=VezaSustava.odgovor;
    }
    
    public void IoT_MasterWork(){
        VezaSustava vs = new VezaSustava();
        vs.posaljiNaSocketu("localhost", 8000, "USER " + korime + "; PASSWD " + lozinka + "; IoT_Master WORK;");
        odgovor=VezaSustava.odgovor;
    }
    
    public void IoT_MasterWait(){
        VezaSustava vs = new VezaSustava();
        vs.posaljiNaSocketu("localhost", 8000, "USER " + korime + "; PASSWD " + lozinka + "; IoT_Master WAIT;");
        odgovor=VezaSustava.odgovor;
    }
    
    public void IoT_MasterStatus(){
        VezaSustava vs = new VezaSustava();
        vs.posaljiNaSocketu("localhost", 8000, "USER " + korime + "; PASSWD " + lozinka + "; IoT_Master STATUS;");
        odgovor=VezaSustava.odgovor;
    }

    public String getOdgovor() {
        return odgovor;
    }

    public void setOdgovor(String odgovor) {
        this.odgovor = odgovor;
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
    
}
