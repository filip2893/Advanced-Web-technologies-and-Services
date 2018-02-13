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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.foi.nwtis.fstrahij.web.kontrole.Izbornik;
import org.foi.nwtis.fstrahij.web.podaci.Korisnik;
import org.foi.nwtis.fstrahij.web.podaci.MeteoPrognoza;
import org.foi.nwtis.fstrahij.web.podaci.Uredjaj;
import org.foi.nwtis.fstrahij.ws.klijenti.PrognozaRESTClient;
import org.foi.nwtis.fstrahij.ws.soap.MeteoWSKlijent;
import org.foi.nwtis.fstrahij.ws.klijenti.UredjajRESTClient;
import org.foi.nwtis.fstrahij.ws.klijenti.UredjajRESTClientContainer;
import org.foi.nwtis.fstrahij.ws.serveri.MeteoPodaci;

/**
 *
 * @author Filip
 */
@Named(value = "odabirIoTPrognoza")
@SessionScoped
public class OdabirIoTPrognoza implements Serializable {
    
    private String noviNaziv;
    private String noviAdresa;
    private List<Izbornik> raspoloziviIoT = new ArrayList<>();    
    private List<Izbornik> odabraniIoT = new ArrayList<>();
    private List<String> popisRaspoloziviIoT = new ArrayList<>();
    private List<String> popisOdabraniIoT = new ArrayList<>();
    private List<MeteoPodaci> meteoPodaci = new ArrayList<>();
    private String azurirajId;
    private String azurirajNaziv;
    private String azurirajAdresa;
    private Date azurirajDatum;
    private boolean azuriranje = false;
    private boolean vazeciMeteo = false;
    private boolean zadnjiMeteo = false;
    private boolean showAdresa = false;
    private String adresa;

    /**
     * Creates a new instance of OdabirIoTPrognoza
     */
    public OdabirIoTPrognoza() {
    }

    public String getNoviNaziv() {
        return noviNaziv;
    }

    public String getNoviAdresa() {
        return noviAdresa;
    }

    public List<Izbornik> getRaspoloziviIoT() {
        dajUredjaje();
        return raspoloziviIoT;
    }

    public List<Izbornik> getOdabraniIoT() {
        return odabraniIoT;
    }

    public String getAzurirajId() {
        return azurirajId;
    }

    public String getAzurirajNaziv() {
        return azurirajNaziv;
    }

    public String getAzurirajAdresa() {
        return azurirajAdresa;
    }

    public Date getAzurirajDatum() {
        return azurirajDatum;
    }

    public void setNoviNaziv(String noviNaziv) {
        this.noviNaziv = noviNaziv;
    }

    public void setNoviAdresa(String noviAdresa) {
        this.noviAdresa = noviAdresa;
    }

    public void setRaspoloziviIoT(List<Izbornik> raspoloziviIoT) {
        this.raspoloziviIoT = raspoloziviIoT;
    }

    public void setOdabraniIoT(List<Izbornik> odabraniIoT) {
        this.odabraniIoT = odabraniIoT;
    }

    public void setAzurirajId(String azurirajId) {
        this.azurirajId = azurirajId;
    }

    public void setAzurirajNaziv(String azurirajNaziv) {
        this.azurirajNaziv = azurirajNaziv;
    }

    public void setAzurirajAdresa(String azurirajAdresa) {
        this.azurirajAdresa = azurirajAdresa;
    }

    public void setAzurirajDatum(Date azurirajDatum) {
        this.azurirajDatum = azurirajDatum;
    }

    public List<String> getPopisRaspoloziviIoT() {
        return popisRaspoloziviIoT;
    }

    public List<String> getPopisOdabraniIoT() {
        return popisOdabraniIoT;
    }

    public void setPopisRaspoloziviIoT(List<String> popisRaspoloziviIoT) {
        this.popisRaspoloziviIoT = popisRaspoloziviIoT;
    }

    public void setPopisOdabraniIoT(List<String> popisOdabraniIoT) {
        this.popisOdabraniIoT = popisOdabraniIoT;
    }    

    public boolean isAzuriranje() {
        return azuriranje;
    }

    public void setAzuriranje(boolean azuriranje) {
        this.azuriranje = azuriranje;
    }

    public boolean isVazeciMeteo() {
        return vazeciMeteo;
    }

    public boolean isZadnjiMeteo() {
        return zadnjiMeteo;
    }

    public boolean isShowAdresa() {
        return showAdresa;
    }

    public void setVazeciMeteo(boolean vazeciMeteo) {
        this.vazeciMeteo = vazeciMeteo;
    }

    public void setZadnjiMeteo(boolean zadnjiMeteo) {
        this.zadnjiMeteo = zadnjiMeteo;
    }

    public void setShowAdresa(boolean showAdresa) {
        this.showAdresa = showAdresa;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public List<MeteoPodaci> getMeteoPodaci() {
        return meteoPodaci;
    }

    public void setMeteoPodaci(List<MeteoPodaci> meteoPodaci) {
        this.meteoPodaci = meteoPodaci;
    }
    
    
    
    public void dajUredjaje(){
        UredjajRESTClientContainer klijent =  new UredjajRESTClientContainer();
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
    
    public void dohvatOdabraniIoTUredaji() {
        if (azuriranje == false) {
            azuriranje = true;
        } else {
            azuriranje = false;
        }
        String id = popisRaspoloziviIoT.get(0);
        int index = raspoloziviIoT.stream()
                .map(s -> s.getVrijednost())
                .collect(Collectors.toList())
                .indexOf(id);
        azurirajId = id;
        azurirajNaziv = raspoloziviIoT.get(index).getLabela();
        PrognozaRESTClient klijent = new PrognozaRESTClient(id);
        azurirajAdresa = klijent.getJson();
    }
    
    public void azurirajUredjaj(){
        if (azurirajId.isEmpty() || azurirajNaziv.isEmpty() || azurirajAdresa.isEmpty()) {
            return;
        }
        UredjajRESTClient klijent = new UredjajRESTClient(azurirajId);
        String odgovor = klijent.putJson("{\"naziv\":\"" + azurirajNaziv + "\""
                    + ",\"adresa\":\"" + azurirajAdresa + "\""
                    + ",\"vrsta\":\"" + "update" + "\"}");
        if (odgovor.equals("1")) {
            System.out.println("uspjesno ažurirano!");
        }else{
             System.out.println("neuspjesno ažurirano!");
        }        
        klijent.close();
    }
    public void noviUredjaj(){
        if (noviNaziv.isEmpty() || noviAdresa.isEmpty()) {
            return;
        }
        String noviId = String.valueOf(raspoloziviIoT.size());
        UredjajRESTClient klijent = new UredjajRESTClient(noviId);
        String odgovor = klijent.putJson("{\"naziv\":\"" + noviNaziv + "\""
                    + ",\"adresa\":\"" + noviAdresa + "\""
                    + ",\"vrsta\":\"" + "insert" + "\"}");
        if (odgovor.equals("1")) {
            System.out.println("uspjesno!");
        }else{
             System.out.println("neuspjesno!");
        }        
        klijent.close();
    }
    
    public void dajAdresu(){
        showAdresa = true;
        vazeciMeteo = false;
        zadnjiMeteo = false;
        
        String id = popisRaspoloziviIoT.get(0);
        adresa = MeteoWSKlijent.dajAdresuZaUredjaj(Integer.parseInt(id));
    }
    
    public void dajVazeceMeteo(){
        showAdresa = false;
        vazeciMeteo = true;
        zadnjiMeteo = false;
        
        String id = popisRaspoloziviIoT.get(0);
        meteoPodaci.clear();
        MeteoWSKlijent
                    .dajMeteoPodatakZaUredjaj(Integer.parseInt(id))
                    .stream()
                    .forEachOrdered(meteoPodaci::add);
        
    }
    
    public void dajZadnjeMeteo(){
        showAdresa = false;
        vazeciMeteo = false;
        zadnjiMeteo = true;
        
        String id = popisRaspoloziviIoT.get(0);
        meteoPodaci.clear();
        MeteoWSKlijent
                    .dajZadnjeMeteoPodatkeZaUredjaj(Integer.parseInt(id))
                    .stream()
                    .forEachOrdered(meteoPodaci::add);
        
    }
}
