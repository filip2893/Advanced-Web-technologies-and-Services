/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import org.foi.nwtis.fstrahij.ejb.eb.Promjene;
import org.foi.nwtis.fstrahij.ejb.sb.PromjeneFacade;

/**
 * Zrno koje služi za obavljanje operacija nad pogledom pregledPromjena.xml
 * @author Filip
 */
@Named(value = "pregledPromjena")
@SessionScoped
public class PregledPromjena implements Serializable {

    @EJB
    private PromjeneFacade promjeneFacade;
    
    private String id, naziv;
    private List<Promjene> promjene = new ArrayList<>();
    /**
     * Creates a new instance of PregledPromjena
     */
    public PregledPromjena() {
    }    
    /**
     * Getter koji dohvaća vrijednost varijable id
     * @return 
     */
    public String getId() {
        return id;
    }
    /**
     * Getter koji dohvaća vrijednost varijable naziv
     * @return 
     */
    public String getNaziv() {
        return naziv;
    }
    /**
     * Setter koji postavlja vrijednost varijable id
     * @param id 
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * Setter koji postavlja vrijednost varijable naziv
     * @param naziv 
     */
    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
    /**
     * Getter koji dohvaća vrijednost liste promjene
     * @return 
     */
    public List<Promjene> getPromjene() {
        return promjene;
    }
    /**
     * Setter koji postavlja vrijednost liste promjene
     * @param promjene 
     */
    public void setPromjene(List<Promjene> promjene) {
        this.promjene = promjene;
    }
    
    /**
     * služi za dohvat podataka o promjenama u listu
     */
    public void preuzmiPodatkeIzPromjena(){
        List<Promjene> p = promjeneFacade.findAll();
        promjene.clear();
        for (Promjene pro : p) {
            promjene.add(new Promjene(pro.getPid(),
                                    pro.getId(),
                                    pro.getNaziv(),
                                    pro.getLatitude(),
                                    pro.getLongitude(),
                                    pro.getStatus(), 
                                    pro.getVrijemePromjene(),
                                    pro.getVrijemeKreiranja()));
        }        
        if (!id.isEmpty()) {
            int id2 =  Integer.parseInt(id);
            promjene = promjene.stream()
                   .filter(s -> s.getPid() == id2)
                   .collect(Collectors.toList());
        }
        if (!naziv.isEmpty()) {
            promjene = promjene.stream()
                   .filter(s -> s.getNaziv().equals(naziv))
                   .collect(Collectors.toList());
        }
    }
}
