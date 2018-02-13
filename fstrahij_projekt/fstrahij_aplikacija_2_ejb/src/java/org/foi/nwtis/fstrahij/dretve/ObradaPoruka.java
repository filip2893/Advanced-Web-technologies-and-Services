/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.dretve;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import org.foi.nwtis.fstrahij.konfiguracije.Konfiguracija;
import org.foi.nwtis.fstrahij.konfiguracije.bp.BP_Konfiguracija;

/**
 * Klasa služi za obradu poruka tj. dretvi. Dretva se starta prilikom
 * inicijaliziranja konteksta i traje određeni ciklus. Sve poruke se obrađuju te
 * se prema obradi kopiraju u odgovaraju foldere te u bazu podataka. Provode se
 * i statističke obrade koje se šalju na određenu mail adrese koja je određena
 * konfiguracijom
 *
 * @author fstrahij
 */
public class ObradaPoruka extends Thread {

    private ServletContext sc = null;
    private boolean stop = false;

    /**
     * Služi za prekid rada dretve
     */
    @Override
    public void interrupt() {
        stop = true;
        super.interrupt();
    }

    /**
     * metoda run pokreće dretvu koja obrađuje poruke
     *
     */
    @Override
    public void run() {
        synchronized (this) {
            while (sc == null) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        Konfiguracija konf = (Konfiguracija) sc.getAttribute("Mail_Konfig");
        String server = konf.dajPostavku("mail.server");
        String port = konf.dajPostavku("mail.port");
        String korisnik = konf.dajPostavku("mail.usernameThread");
        String lozinka = konf.dajPostavku("mail.passwordThread");
        int trajanjeCiklusa = Integer.parseInt(konf.dajPostavku("mail.timeSecThread"));
        String predmet = konf.dajPostavku("mail.subject");
        String NWTiS_Poruke = konf.dajPostavku("mail.folderNWTiS");
        String NWTiS_OstalePoruke = konf.dajPostavku("mail.folderOther");

        String korisnikStatistika = konf.dajPostavku("mail.usernameStatistics");
        String predmetStatistika = konf.dajPostavku("mail.subjectStatistics");

        int redniBrojCiklusa = 0;
       
        while (!stop) {
            redniBrojCiklusa++;
            long startTime = System.currentTimeMillis();
            System.out.println("Ciklus dretve ObradaPoruka: " + redniBrojCiklusa);
            
            try {
                long trajanjeObrade = System.currentTimeMillis();
                trajanjeObrade = trajanjeObrade - startTime;

                sleep(trajanjeCiklusa * 1000 - trajanjeObrade);
            } catch (InterruptedException ex) {
                Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Pokreće dretvu
     */
    @Override
    public synchronized void start() {
        super.start();
    }

    /**
     * Služi za startanje dretve
     *
     * @param sc
     */
    public synchronized void setSc(ServletContext sc) {
        this.sc = sc;
        notify();
    }

}
