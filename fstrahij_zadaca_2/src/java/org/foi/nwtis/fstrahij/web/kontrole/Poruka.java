/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.web.kontrole;

import java.util.Date;

/**
 * Klasa koja služi za spremanje i dohvaćanje vrijednosti poruka koje se šalju
 * mailom
 *
 * @author fstrahij
 */
public class Poruka {

    private String id;
    private Date vrijemeSlanja;
    private Date vrijemePrijema;
    private String salje;
    private String predmet;
    private String sadrzaj;
    private String vrsta;

    /**
     * Konstruktor klase pomoću kojeg se postavljaju vrijednosti svih varijabli
     * u klasi
     *
     * @param id
     * @param vrijemeSlanja
     * @param vrijemePrijema
     * @param salje
     * @param predmet
     * @param sadrzaj
     * @param vrsta
     */
    public Poruka(String id, Date vrijemeSlanja, Date vrijemePrijema, String salje, String predmet, String sadrzaj, String vrsta) {
        this.id = id;
        this.vrijemeSlanja = vrijemeSlanja;
        this.vrijemePrijema = vrijemePrijema;
        this.salje = salje;
        this.predmet = predmet;
        this.sadrzaj = sadrzaj;
        this.vrsta = vrsta;
    }

    /**
     * Getter koji vraća vrijednost varijable id
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * Getter koji vraća vrijednost varijable vrijemeSlanja
     *
     * @return
     */
    public Date getVrijemeSlanja() {
        return vrijemeSlanja;
    }

    /**
     * Getter koji vraća vrijednost varijable vrijemePrijema
     *
     * @return
     */
    public Date getVrijemePrijema() {
        return vrijemePrijema;
    }

    /**
     * Getter koji vraća vrijednost varijable predmet
     *
     * @return
     */
    public String getPredmet() {
        return predmet;
    }

    /**
     * Getter koji vraća vrijednost varijable salje
     *
     * @return
     */
    public String getSalje() {
        return salje;
    }

    /**
     * Getter koji vraća vrijednost varijable vrsta
     *
     * @return
     */
    public String getVrsta() {
        return vrsta;
    }

    /**
     * Getter koji vraća vrijednost varijable sadrzaj
     *
     * @return
     */
    public String getSadrzaj() {
        return sadrzaj;
    }

}
