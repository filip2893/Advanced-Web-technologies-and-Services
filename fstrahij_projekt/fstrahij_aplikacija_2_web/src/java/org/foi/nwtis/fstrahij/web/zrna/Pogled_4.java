/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import javax.faces.context.FacesContext;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.fstrahij.server.socket.VezaSustava;
import org.foi.nwtis.fstrahij.web.kontrole.Izbornik;
import org.foi.nwtis.fstrahij.web.podaci.Korisnik;
import org.foi.nwtis.fstrahij.web.podaci.Uredjaj;
import org.foi.nwtis.fstrahij.ws.klijenti.PrognozaRESTClient;
import org.foi.nwtis.fstrahij.ws.klijenti.UredjajRESTClientContainer;

/**
 *
 * @author Filip
 */
@Named(value = "pogled_4")
@SessionScoped
public class Pogled_4 implements Serializable {

    private List<Izbornik> raspoloziviIoT = new ArrayList<>();
    private List<Izbornik> listaIoT = new ArrayList<>();
    private List<String> popisRaspoloziviIoT = new ArrayList<>();
    private List<String> popisListaIoT = new ArrayList<>();
    String id, naziv, adresa, odgovor = "";
    private String korime, lozinka;

    /**
     * Creates a new instance of Pogled_3
     */
    public Pogled_4() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        korime = session.getAttribute("korime").toString();
        lozinka = session.getAttribute("lozinka").toString();
    }
    
    public void ioT_MasterLoad(){
        VezaSustava vs = new VezaSustava();
        vs.posaljiNaSocketu("localhost", 8000, "USER " + korime + "; PASSWD " + lozinka + "; IoT_Master LOAD;");  
        odgovor = VezaSustava.odgovor;
    }
    
    public void ioT_MasterClear(){
        VezaSustava vs = new VezaSustava();
        vs.posaljiNaSocketu("localhost", 8000, "USER " + korime + "; PASSWD " + lozinka + "; IoT_Master CLEAR;");    
        odgovor = VezaSustava.odgovor;
    }
    
    public void ioT_MasterList(){
        VezaSustava vs = new VezaSustava();
        vs.posaljiNaSocketu("localhost", 8000, "USER " + korime + "; PASSWD " + lozinka + "; IoT_Master LIST;");
        parsirajOdgovorJson();
    }

    public void ioT_Add() {
        System.out.println("ADD");
        dohvatOdabraniIoTUredajiAdd();
        
        VezaSustava vs = new VezaSustava();
        vs.posaljiNaSocketu("localhost", 8000, "USER " + korime + "; PASSWD " + lozinka + "; IoT " + id + " ADD \"" + naziv + "\" "+ adresa+"" + ";");        
        odgovor = VezaSustava.odgovor;
    }

    public void ioT_Work(){
        dohvatOdabraniIoTUredaji();
        VezaSustava vs = new VezaSustava();
        vs.posaljiNaSocketu("localhost", 8000, "USER " + korime + "; PASSWD " + lozinka + "; IoT " + id + " WORK;");        
        odgovor = VezaSustava.odgovor;
    }

    public void ioT_Wait(){
        dohvatOdabraniIoTUredaji();
        VezaSustava vs = new VezaSustava();
        vs.posaljiNaSocketu("localhost", 8000, "USER " + korime + "; PASSWD " + lozinka + "; IoT " + id + " WAIT;");       
        odgovor = VezaSustava.odgovor;
    }

    public void ioT_Remove() {
        dohvatOdabraniIoTUredaji();
        VezaSustava vs = new VezaSustava();
        vs.posaljiNaSocketu("localhost", 8000, "USER " + korime + "; PASSWD " + lozinka + "; IoT " + id + " REMOVE;");        
        odgovor = VezaSustava.odgovor;
    }

    public void ioT_Status() {
        dohvatOdabraniIoTUredaji();
        VezaSustava vs = new VezaSustava();
        vs.posaljiNaSocketu("localhost", 8000, "USER " + korime + "; PASSWD " + lozinka + "; IoT " + id + " STATUS;");      
        odgovor = VezaSustava.odgovor;
    }

    public void dajUredjaje() {
        UredjajRESTClientContainer klijent = new UredjajRESTClientContainer();
        String sviUredjaji = klijent.getJson();

        JsonReader reader = Json.createReader(new StringReader(sviUredjaji));

        JsonArray jsonProg = reader.readArray();
        raspoloziviIoT.clear();
        for (int i = 0; i < jsonProg.size(); i++) {
            JsonObject lista = jsonProg.getJsonObject(i);
            Uredjaj uredjaj = new Uredjaj();

            uredjaj.setId(lista.getInt("uid"));
            uredjaj.setNaziv(lista.getString("naziv"));
            raspoloziviIoT.add(new Izbornik(uredjaj.getNaziv(), String.valueOf(uredjaj.getId())));
        }
    }

    public void dohvatOdabraniIoTUredaji(){
        String id = popisListaIoT.get(0);
        int index = listaIoT.stream()
                .map(s -> s.getVrijednost())
                .collect(Collectors.toList())
                .indexOf(id);
        this.id = id;
        naziv = listaIoT.get(index).getLabela();
        PrognozaRESTClient klijent = new PrognozaRESTClient(id);   
        adresa = klijent.getJson();
    }
    
     public void dohvatOdabraniIoTUredajiAdd(){
        String id = popisRaspoloziviIoT.get(0);
        int index = raspoloziviIoT.stream()
                .map(s -> s.getVrijednost())
                .collect(Collectors.toList())
                .indexOf(id);
        this.id = id;
        naziv = raspoloziviIoT.get(index).getLabela();
        PrognozaRESTClient klijent = new PrognozaRESTClient(id);   
        adresa = klijent.getJson();
    }
    
    
    private void parsirajOdgovorJson(){
        listaIoT.clear();
        if (VezaSustava.odgovor.equals("OK 10")) {
            odgovor = VezaSustava.odgovor;
        }
        else{
            JsonReader reader = Json.createReader(new StringReader(VezaSustava.odgovor));

            JsonArray jsonProg = reader.readArray();

            for (int i = 0; i < jsonProg.size(); i++) {
                JsonObject lista = jsonProg.getJsonObject(i);

                String naziv = lista.getString("naziv").trim();
                String idu = lista.getString("id");
                listaIoT.add(new Izbornik(naziv, idu));
            }
        }
    }

    public String getOdgovor() {
        return odgovor;
    }

    public void setOdgovor(String odgovor) {
        this.odgovor = odgovor;
    }
    

    public List<Izbornik> getRaspoloziviIoT() {
        dajUredjaje();
        return raspoloziviIoT;
    }

    public List<Izbornik> getListaIoT() {
        return listaIoT;
    }

    public List<String> getPopisRaspoloziviIoT() {
        return popisRaspoloziviIoT;
    }

    public List<String> getPopisListaIoT() {
        return popisListaIoT;
    }

    public String getId() {
        return id;
    }

    public String getNaziv() {
        return naziv;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setRaspoloziviIoT(List<Izbornik> raspoloziviIoT) {
        this.raspoloziviIoT = raspoloziviIoT;
    }

    public void setListaIoT(List<Izbornik> listaIoT) {
        this.listaIoT = listaIoT;
    }

    public void setPopisRaspoloziviIoT(List<String> popisRaspoloziviIoT) {
        this.popisRaspoloziviIoT = popisRaspoloziviIoT;
    }

    public void setPopisListaIoT(List<String> popisListaIoT) {
        this.popisListaIoT = popisListaIoT;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
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
