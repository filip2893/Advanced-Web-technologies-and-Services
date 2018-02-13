/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import org.foi.nwtis.fstrahij.konfiguracije.Konfiguracija;

/**
 * Klasa koja služi za uspostavljanje veze mail serveru i slanje poruke
 *
 * @author fstrahij
 */
@Named(value = "slanjePoruke")
@RequestScoped
public class SlanjePoruke {

    private String posluzitelj;
    private String salje;
    private String prima;
    private String predmet;
    private String sadrzaj;

    /**
     * Creates a new instance of SlanjePoruke
     */
    public SlanjePoruke() {

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
     * Setter koji postavlja vrijednost varijable salje
     *
     * @param salje
     */
    public void setSalje(String salje) {
        this.salje = salje;
    }

    /**
     * Getter koji vraća vrijednost varijable prima
     *
     * @return
     */
    public String getPrima() {
        return prima;
    }

    /**
     * Setter koji postavlja vrijednost varijable prima
     *
     * @param prima
     */
    public void setPrima(String prima) {
        this.prima = prima;
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
     * Setter koji postavlja vrijednost varijable predmet
     *
     * @param predmet
     */
    public void setPredmet(String predmet) {
        this.predmet = predmet;
    }

    /**
     * Getter koji vraća vrijednost varijable sadrzaj
     *
     * @return
     */
    public String getSadrzaj() {
        return sadrzaj;
    }

    /**
     * Setter koji postavlja vrijednost varijable sadrzaj
     *
     * @param sadrzaj
     */
    public void setSadrzaj(String sadrzaj) {
        this.sadrzaj = sadrzaj;
    }

    /**
     * Medota koja služi za slanje poruke na mail server. Postavke se dohvaćaju
     * iz datoteke konfiguracije
     *
     * @return
     */
    public String saljiPoruku() {
        ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        Konfiguracija konfig = (Konfiguracija) context.getAttribute("Mail_Konfig");

        this.posluzitelj = konfig.dajPostavku("mail.server");
        //TODO dodaj za slanje poruke prema primjeru s predavnja koji je priložen uz zadaću        
        String status = null;
        try {
            java.util.Properties properties = System.getProperties();
            properties.put("mail.imap.host", this.posluzitelj);

            Session session = Session.getInstance(properties, null);

            MimeMessage message = new MimeMessage(session);

            Address fromAddress = new InternetAddress(this.salje);
            message.setFrom(fromAddress);

            Address[] toAddresses = InternetAddress.parse(this.prima);
            message.setRecipients(Message.RecipientType.TO, toAddresses);

            message.setSubject(this.predmet);
            message.setText(this.sadrzaj);

            Transport.send(message);

            status = "Poruka poslana.";

        } catch (AddressException e) {
            e.printStackTrace();
            status = "Greska kod parsiranja adrese.";
        } catch (SendFailedException e) {
            e.printStackTrace();
            status = "Greska kod slanja maila.";
        } catch (MessagingException e) {
            e.printStackTrace();
            status = "Nepredvidjena greska.";
        }

        return status;
    }

    /**
     * vraća string
     *
     * @return
     */
    public String pocetna() {
        return "pocetna";
    }

    /**
     * vraća string
     *
     * @return
     */
    public String pregledPoruka() {
        return "pregledPoruka";
    }
}
