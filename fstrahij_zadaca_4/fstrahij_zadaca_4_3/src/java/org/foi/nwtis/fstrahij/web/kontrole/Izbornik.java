/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.web.kontrole;

/**
 * Klasa služi za prikaz u izbornicima
 *
 * @author fstrahij
 */
public class Izbornik {

    private String labela;
    private String vrijednost;

    /**
     * Kontruktor pomoću kojeg se dohvaća labela i vrijednost
     *
     * @param labela
     * @param vrijednost
     */
    public Izbornik(String labela, String vrijednost) {
        this.labela = labela;
        this.vrijednost = vrijednost;
    }

    /**
     * Getter za dohvaćanje vrijednosti varijable labela
     *
     * @return
     */
    public String getLabela() {
        return labela;
    }

    /**
     * Setter za postavljanje vrijednosti varijable labela
     *
     * @param labela
     */
    public void setLabela(String labela) {
        this.labela = labela;
    }

    /**
     * Getter za dohvaćanje varijable vrijednost
     *
     * @return
     */
    public String getVrijednost() {
        return vrijednost;
    }

    /**
     * Setter za postavljanje vrijednosti varijable vrijednost
     *
     * @param vrijednost
     */
    public void setVrijednost(String vrijednost) {
        this.vrijednost = vrijednost;
    }

}
