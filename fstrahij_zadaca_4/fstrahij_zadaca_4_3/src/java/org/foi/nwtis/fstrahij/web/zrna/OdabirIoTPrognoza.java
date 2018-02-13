/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import org.foi.nwtis.fstrahij.ejb.eb.Promjene;
import org.foi.nwtis.fstrahij.ejb.eb.Uredaji;
import org.foi.nwtis.fstrahij.ejb.sb.MeteoIoTKlijent;
import org.foi.nwtis.fstrahij.ejb.sb.PromjeneFacade;
import org.foi.nwtis.fstrahij.ejb.sb.UredajiFacade;
import org.foi.nwtis.fstrahij.web.podaci.Lokacija;
import org.foi.nwtis.fstrahij.web.kontrole.Izbornik;
import org.foi.nwtis.fstrahij.web.podaci.MeteoPrognoza;

/**
 * Zrno koje obavlja razne operacije nad pogledom pregledIoT.xml
 *
 * @author Filip
 */
@Named(value = "odabirIoTPrognoza")
@SessionScoped
public class OdabirIoTPrognoza implements Serializable {

    @EJB
    private PromjeneFacade promjeneFacade;

    @EJB
    private MeteoIoTKlijent meteoIoTKlijent;

    @EJB
    private UredajiFacade uredajiFacade; 

    private String noviId;
    private String noviNaziv;
    private String noviAdresa;
    private List<Izbornik> raspoloziviIoT = new ArrayList<>();
    private List<Izbornik> odabraniIoT = new ArrayList<>();
    private List<String> popisRaspoloziviIoT = new ArrayList<>();
    private List<String> popisOdabraniIoT = new ArrayList<>();
    private List<MeteoPrognoza> meteoPrognoze = new ArrayList<>();
    private boolean azuriranje = false;
    private boolean prognoze = false;
    private String azurirajId;
    private String azurirajNaziv;
    private String azurirajAdresa;
    private Date azurirajDatum;
    private String gumbPregledPrognoza = "Pregled prognoza";
    private String pregledPogreska = "";

    /**
     * Creates a new instance of OdabirIoTPrognoza
     */
    public OdabirIoTPrognoza() {
    }

    /**
     * Getter koji vraća vrijednost varijable noviId
     *
     * @return
     */
    public String getNoviId() {
        return noviId;
    }

    /**
     * Postavlja vrijednost varijable noviId
     *
     * @param noviId
     */
    public void setNoviId(String noviId) {
        this.noviId = noviId;
    }

    /**
     * Getter koji vraća vrijednost varijable noviNaziv
     *
     * @return
     */
    public String getNoviNaziv() {
        return noviNaziv;
    }

    /**
     * Postavlja vrijednost varijable noviNaziv
     *
     * @param noviNaziv
     */
    public void setNoviNaziv(String noviNaziv) {
        this.noviNaziv = noviNaziv;
    }

    /**
     * Getter koji vraća vrijednost varijable noviAdresa
     *
     * @return
     */
    public String getNoviAdresa() {
        return noviAdresa;
    }

    /**
     * Postavlja vrijednost varijable noviAdresa
     *
     * @param noviAdresa
     */
    public void setNoviAdresa(String noviAdresa) {
        this.noviAdresa = noviAdresa;
    }

    /**
     * Getter koji vraća vrijednost liste raspoloziviIoT
     *
     * @return
     */
    public List<Izbornik> getRaspoloziviIoT() {
        preuzmiRaspoloziveIoTUredaje();
        return raspoloziviIoT;
    }

    /**
     * Postavlja vrijednost liste raspoloziviIoT
     *
     * @param raspoloziviIoT
     */
    public void setRaspoloziviIoT(List<Izbornik> raspoloziviIoT) {
        this.raspoloziviIoT = raspoloziviIoT;
    }

    /**
     * Getter koji vraća vrijednost liste odabraniIoT
     *
     * @return
     */
    public List<Izbornik> getOdabraniIoT() {
        return odabraniIoT;
    }

    /**
     * Postavlja vrijednost liste odabraniIoT
     *
     * @param odabraniIoT
     */
    public void setOdabraniIoT(List<Izbornik> odabraniIoT) {
        this.odabraniIoT = odabraniIoT;
    }

    /**
     * Getter koji vraća vrijednost liste popisRaspoloziviIoT
     *
     * @return
     */
    public List<String> getPopisRaspoloziviIoT() {
        return popisRaspoloziviIoT;
    }

    /**
     * Postavlja vrijednost liste popisRaspoloziviIoT
     *
     * @param popisRaspoloziviIoT
     */
    public void setPopisRaspoloziviIoT(List<String> popisRaspoloziviIoT) {
        this.popisRaspoloziviIoT = popisRaspoloziviIoT;
    }

    /**
     * Getter koji vraća vrijednost liste popisOdabraniIoT
     *
     * @return
     */
    public List<String> getPopisOdabraniIoT() {
        return popisOdabraniIoT;
    }

    /**
     * Postavlja vrijednost liste popisOdabraniIoT
     *
     * @param popisOdabraniIoT
     */
    public void setPopisOdabraniIoT(List<String> popisOdabraniIoT) {
        this.popisOdabraniIoT = popisOdabraniIoT;
    }

    /**
     * Getter koji vraća vrijednost liste meteoPrognoze
     *
     * @return
     */
    public List<MeteoPrognoza> getMeteoPrognoze() {
        return meteoPrognoze;
    }

    /**
     * Postavlja vrijednost liste meteoPrognoze
     *
     * @param meteoPrognoze
     */
    public void setMeteoPrognoze(List<MeteoPrognoza> meteoPrognoze) {
        this.meteoPrognoze = meteoPrognoze;
    }

    /**
     * Vraća vrijednost varijable azuriranje
     *
     * @return
     */
    public boolean isAzuriranje() {
        return azuriranje;
    }

    /**
     * Postavlja vrijednost varijable azuriranje
     *
     * @param azuriranje
     */
    public void setAzuriranje(boolean azuriranje) {
        this.azuriranje = azuriranje;
    }

    /**
     * Vraća vrijednost varijable prognoze
     *
     * @return
     */
    public boolean isPrognoze() {
        return prognoze;
    }

    /**
     * Postavlja vrijednost varijable prognoze
     *
     * @param prognoze
     */
    public void setPrognoze(boolean prognoze) {
        this.prognoze = prognoze;
    }

    /**
     * Getter koji dohvaća vrijednost varijable azurirajId
     *
     * @return
     */
    public String getAzurirajId() {
        return azurirajId;
    }

    /**
     * Setter koji postavlja vrijednost varijable azurirajId
     *
     * @param azurirajId
     */
    public void setAzurirajId(String azurirajId) {
        this.azurirajId = azurirajId;
    }

    /**
     * Getter koji dohvaća vrijednost varijable azurirajNaziv
     *
     * @return
     */
    public String getAzurirajNaziv() {
        return azurirajNaziv;
    }

    /**
     * Setter koji postavlja vrijednost varijable azurirajNaziv
     *
     * @param azurirajNaziv
     */
    public void setAzurirajNaziv(String azurirajNaziv) {
        this.azurirajNaziv = azurirajNaziv;
    }

    /**
     * Getter koji dohvaća vrijednost varijable azurirajAdresa
     *
     * @return
     */
    public String getAzurirajAdresa() {
        return azurirajAdresa;
    }

    /**
     * Setter koji postavlja vrijednost varijable azurirajAdresa
     *
     * @param azurirajAdresa
     */
    public void setAzurirajAdresa(String azurirajAdresa) {
        this.azurirajAdresa = azurirajAdresa;
    }

    /**
     * Getter koji dohvaća vrijednost varijable gumbPregledPrognoza
     *
     * @return
     */
    public String getGumbPregledPrognoza() {
        return gumbPregledPrognoza;
    }

    /**
     * Setter koji postavlja vrijednost varijable gumbPregledPrognoza
     *
     * @param gumbPregledPrognoza
     */
    public void setGumbPregledPrognoza(String gumbPregledPrognoza) {
        this.gumbPregledPrognoza = gumbPregledPrognoza;
    }

    /**
     * Getter koji dohvaća vrijednost varijable azurirajDatum
     *
     * @return
     */
    public Date getAzurirajDatum() {
        return azurirajDatum;
    }

    /**
     * Setter koji postavlja vrijednost varijable azurirajDatum
     *
     * @param azurirajDatum
     */
    public void setAzurirajDatum(Date azurirajDatum) {
        this.azurirajDatum = azurirajDatum;
    }

    /**
     * Getter koji dohvaća vrijednost varijable pregledPogreska
     *
     * @return
     */
    public String getPregledPogreska() {
        return pregledPogreska;
    }

    /**
     * Setter koji postavlja vrijednost varijable pregledPogreska
     *
     * @param pregledPogreska
     */
    public void setPregledPogreska(String pregledPogreska) {
        this.pregledPogreska = pregledPogreska;
    }

    /**
     * Dohvaća odabrane IoT i stavlja u listu odabraniIoT, te briše iz liste
     * raspoloziviIoT
     */
    public void preuzmiOdabraniIoTUredaji() {
        for (int i = 0; i < popisRaspoloziviIoT.size(); i++) {
            String id = popisRaspoloziviIoT.get(i);
            int index = raspoloziviIoT.stream()
                    .map(s -> s.getVrijednost())
                    .collect(Collectors.toList())
                    .indexOf(id);
            odabraniIoT.add(new Izbornik(raspoloziviIoT.get(index).getLabela(), raspoloziviIoT.get(index).getVrijednost()));
            raspoloziviIoT.remove(index);
        }
    }

    /**
     * Dohvaća odabrane IoT i stavlja u listu raspoloziviIoT, te briše iz liste
     * odabraniIoT
     */
    public void vratiOdabraniIoTUredaji() {
        for (int i = 0; i < popisOdabraniIoT.size(); i++) {
            String id = popisOdabraniIoT.get(i);
            int index = odabraniIoT.stream()
                    .map(s -> s.getVrijednost())
                    .collect(Collectors.toList())
                    .indexOf(id);
            raspoloziviIoT.add(new Izbornik(odabraniIoT.get(index).getLabela(), odabraniIoT.get(index).getVrijednost()));
            odabraniIoT.remove(index);
        }
    }

    /**
     * služi za dohvat odabranih IoT iz liste raspoloziviIoT
     */
    public void dohvatOdabraniIoTUredaji() {
        if (azuriranje == false) {
            azuriranje = true;
        } else {
            azuriranje = false;
        }
        String id = popisRaspoloziviIoT.get(0);
        List<Uredaji> uredaji = uredajiFacade.findAll();
        int index = raspoloziviIoT.stream()
                .map(s -> s.getVrijednost())
                .collect(Collectors.toList())
                .indexOf(id);
        azurirajId = uredaji.get(index).getId().toString();
        azurirajNaziv = uredaji.get(index).getNaziv();
        String lat = String.valueOf(uredaji.get(index).getLatitude());
        String lon = String.valueOf(uredaji.get(index).getLongitude());
        azurirajAdresa = meteoIoTKlijent.dajAdresu(lat, lon);
        azurirajDatum = uredaji.get(index).getVrijemeKreiranja();
    }

    /**
     * služi za ažuriranje odabranog uređaja iz liste raspoloziviIoT
     *
     * @throws ParseException
     */
    public void azurirajOdabraniIoTUredaji() throws ParseException {
        if (azurirajId.isEmpty() || azurirajNaziv.isEmpty() || azurirajAdresa.isEmpty()) {
            pregledPogreska = "Sva polja moraju biti popunjena za azuriranje";
            return;
        } else {
            pregledPogreska = "";
        }
        Lokacija l = meteoIoTKlijent.dajLokaciju(azurirajAdresa);
        System.err.println("lokacija: " + l.getLatitude());
        float lat = Float.parseFloat(l.getLatitude());
        float lng = Float.parseFloat(l.getLongitude());

        Uredaji uredaji = new Uredaji();
        uredaji.setId(Integer.parseInt(azurirajId));
        uredaji.setNaziv(azurirajNaziv);
        uredaji.setLatitude(lat);
        uredaji.setLongitude(lng);
        uredaji.setStatus(0);
        uredaji.setVrijemePromjene(new Date());
        uredaji.setVrijemeKreiranja(azurirajDatum);
        
        Promjene promjene = new Promjene();
        promjene.setId(uredaji.getId());
        promjene.setNaziv(uredaji.getNaziv());
        promjene.setLatitude(lat);
        promjene.setLongitude(lng);
        promjene.setStatus(0);
        promjene.setVrijemeKreiranja(azurirajDatum);
        promjene.setVrijemePromjene(new Date());
        
        promjeneFacade.create(promjene);
        uredajiFacade.edit(uredaji);
        odabraniIoT.clear();
    }

    /**
     * preuzima raspoložive IoT iz baze
     */
    public void preuzmiRaspoloziveIoTUredaje() {
        List<Uredaji> uredaji = uredajiFacade.findAll();
        raspoloziviIoT.clear();
        for (Uredaji uredaj : uredaji) {
            raspoloziviIoT.add(new Izbornik(uredaj.getNaziv(), uredaj.getId().toString()));
        }

    }

    /**
     * služi za dodavanje novog IoT
     *
     * @return
     */
    public String dodajIoTUredaj() {
        if (noviId.isEmpty() || noviNaziv.isEmpty() || noviAdresa.isEmpty()) {
            pregledPogreska = "Sva polja moraju biti popunjena za dodavnje novog IoT uredjaja";
            return null;
        } else {
            pregledPogreska = "";
        }
        Lokacija l = meteoIoTKlijent.dajLokaciju(noviAdresa);
        System.err.println("lokacija: " + l.getLatitude());
        float lat = Float.parseFloat(l.getLatitude());
        float lng = Float.parseFloat(l.getLongitude());
        Uredaji uredaji = new Uredaji(Integer.parseInt(noviId), noviNaziv, lat, lng, 0,
                new Date(), new Date());
        
        Promjene promjene = new Promjene();
        promjene.setId(uredaji.getId());
        promjene.setNaziv(uredaji.getNaziv());
        promjene.setLatitude(lat);
        promjene.setLongitude(lng);
        promjene.setStatus(0);
        promjene.setVrijemeKreiranja(new Date());
        promjene.setVrijemePromjene(new Date());
        
        promjeneFacade.create(promjene);
        uredajiFacade.create(uredaji);
        return "";
    }

    /**
     * služi za preuzimanje prognoze za odabrane IoT iz liste odabraniIoT
     */
    public void preuzmiPrognoze() {
        if (gumbPregledPrognoza.equals("Zatvori prognoze")) {
            prognoze = false;
            gumbPregledPrognoza = "Pregled prognoza";
        } else {
            prognoze = true;
            gumbPregledPrognoza = "Zatvori prognoze";
        }
        meteoPrognoze.clear();
        List<Uredaji> uredaji = uredajiFacade.findAll();
        List<Uredaji> uredaj = new ArrayList<>();
        for (int i = 0; i < odabraniIoT.size(); i++) {
            int id = Integer.parseInt(odabraniIoT.get(i).getVrijednost());
            uredaji.stream()
                    .filter(s -> s.getId() == id)
                    .forEachOrdered(uredaj::add);
            String lat = String.valueOf(uredaj.get(i).getLatitude());
            String lon = String.valueOf(uredaj.get(i).getLongitude());
            String adresa = meteoIoTKlijent.dajAdresu(lat, lon);
            for (MeteoPrognoza mp : meteoIoTKlijent.dajMeteoPrognoze(adresa, id)) {
                if (mp != null) {
                    meteoPrognoze.add(mp);
                }
            }
        }
    }

}
