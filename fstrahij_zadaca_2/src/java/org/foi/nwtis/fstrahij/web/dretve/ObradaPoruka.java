/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.web.dretve;

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

        BP_Konfiguracija bpkonf = (BP_Konfiguracija) sc.getAttribute("BP_Konfig");
        String serverDB = bpkonf.getServerDatabase();
        String baza = serverDB + bpkonf.getUserDatabase();
        String korisnikDB = bpkonf.getUserUsername();
        String lozinkaDB = bpkonf.getUserPassword();
        String driver = bpkonf.getDriverDatabase();
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql = "select * from uredaji";

        int redniBrojCiklusa = 0;

        String add = "^ADD IoT ([0-9]{1,6}) \\\"([a-zA-Z0-9ĆČŽŠĐćčšžđ\\s]{1,30})\\\"(GPS:)?\\s?([0-9]{1,3}.[0-9]{6})?,?([0-9]{1,3}.[0-9]{6})?;$";
        String temp = "^TEMP IoT ([0-9]{1,6}) T:\\s([0-9]{4}.[0-9]{2}.[0-9]{2}\\s[0-9]{2}:[0-9]{2}:[0-9]{2})(\\s[C]:\\s)?([0-9]{1,2}.[0-9]{1,2})?;$";
        String event = "^EVENT IoT ([0-9]{1,6}) T:\\s([0-9]{4}.[0-9]{2}.[0-9]{2}\\s[0-9]{2}:[0-9]{2}:[0-9]{2})(\\s[F]:\\s)([0-9]{1,2});$";

        while (!stop) {
            redniBrojCiklusa++;
            String obradaZapocela = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss.SSS").format(new Date());
            int porukaBr = 0, IoT = 0, tempBr = 0, eventBr = 0, pogreske = 0;
            long startTime = System.currentTimeMillis();

            System.out.println("Ciklus dretve ObradaPoruka: " + redniBrojCiklusa);
            try {

                // Start the session
                java.util.Properties properties = System.getProperties();
                properties.put("mail.imap.host", server);
                Session session = Session.getInstance(properties, null);

                // Connect to the store
                Store store = session.getStore("imap");
                store.connect(server, korisnik, lozinka);

                // Open the INBOX folder
                Folder folder = store.getFolder("INBOX");
                folder.open(Folder.READ_WRITE);
                Folder defaultFolder = store.getDefaultFolder();
                if (!store.getFolder(NWTiS_Poruke).exists()) {
                    Folder newFolder = defaultFolder.getFolder(NWTiS_Poruke);
                    newFolder.create(Folder.HOLDS_MESSAGES);
                }

                if (!store.getFolder(NWTiS_OstalePoruke).exists()) {
                    Folder newFolder = defaultFolder.getFolder(NWTiS_OstalePoruke);
                    newFolder.create(Folder.HOLDS_MESSAGES);
                }

                Message[] messages = folder.getMessages();
                int kolPoruka = messages.length;
                for (int i = 0; i < kolPoruka; ++i) {
                    porukaBr = kolPoruka;
                    String sadrzaj = messages[i].getContent().toString().trim();
                    if (predmet.equals(messages[i].getSubject())) {
                        try (Connection veza = DriverManager.getConnection(baza, korisnikDB, lozinkaDB);) {

                            Statement naredba;
                            ResultSet odgovor;
                            String naziv = null;
                            float latitude = 0, longitude = 0;
                            int id = 0;

                            Pattern p = Pattern.compile(add);
                            Matcher m = p.matcher(sadrzaj);
                            boolean status = m.matches();
                            if (status) {
                                id = Integer.parseInt(m.group(1));
                                naziv = m.group(2);
                                if (m.group(4) != null) {
                                    latitude = Float.valueOf(m.group(4));
                                }
                                if (m.group(5) != null) {
                                    longitude = Float.valueOf(m.group(5));
                                }
                                sql = "SELECT * FROM uredaji where id=" + id;
                                naredba = veza.createStatement();
                                odgovor = naredba.executeQuery(sql);

                                Folder NWTiS_Poruke_Folder = store.getFolder(NWTiS_Poruke);
                                NWTiS_Poruke_Folder.open(Folder.READ_WRITE);
                                folder.copyMessages(folder.getMessages(i + 1, i + 1), NWTiS_Poruke_Folder);

                                Flags deleted = new Flags(Flags.Flag.DELETED);
                                folder.setFlags(folder.getMessages(i + 1, i + 1), deleted, true);
                                folder.expunge();

                                if (odgovor.next()) {
                                    System.err.println("(ADD) U bazi postoji zapis s tim id: " + id);
                                    pogreske++;
                                } else {
                                    id = 0;
                                    sql = "SELECT id FROM `uredaji` ORDER BY id DESC LIMIT 1";
                                    naredba = veza.createStatement();
                                    odgovor = naredba.executeQuery(sql);
                                    while (odgovor.next()) {
                                        id = odgovor.getInt("id");
                                    }
                                    id++;
                                    sql = "INSERT INTO uredaji (id,naziv,latitude,longitude) VALUES \n"
                                            + " (" + id + ",'" + naziv + "'," + latitude + "," + longitude + ");";
                                    naredba = veza.createStatement();
                                    naredba.executeUpdate(sql);
                                }
                            } else {
                                p = Pattern.compile(temp);
                                m = p.matcher(sadrzaj);
                                status = m.matches();
                                if (status) {
                                    Folder NWTiS_Poruke_Folder = store.getFolder(NWTiS_Poruke);
                                    NWTiS_Poruke_Folder.open(Folder.READ_WRITE);
                                    folder.copyMessages(folder.getMessages(i + 1, i + 1), NWTiS_Poruke_Folder);

                                    Flags deleted = new Flags(Flags.Flag.DELETED);
                                    folder.setFlags(folder.getMessages(i + 1, i + 1), deleted, true);
                                    folder.expunge();

                                    tempBr++;

                                    id = Integer.parseInt(m.group(1));
                                    String vrijemeKreiranja = m.group(2);
                                    vrijemeKreiranja = vrijemeKreiranja.replace(".", "-");
                                    float temperatura = 0;
                                    if (m.group(4) != null) {
                                        temperatura = Float.valueOf(m.group(4));
                                    }
                                    sql = "SELECT * FROM uredaji where id=" + id;
                                    naredba = veza.createStatement();
                                    odgovor = naredba.executeQuery(sql);

                                    if (odgovor.next()) {
                                        sql = "INSERT INTO temperature (id, temp, vrijeme_kreiranja) VALUES(" + id + "," + temperatura + ",'" + Timestamp.valueOf(vrijemeKreiranja) + "');";
                                        naredba = veza.createStatement();
                                        naredba.executeUpdate(sql);
                                        IoT++;
                                    } else {
                                        System.err.println("(TEMP) U bazi NE postoji zapis s tim id: " + id);
                                        pogreske++;
                                    }
                                } else {
                                    p = Pattern.compile(event);
                                    m = p.matcher(sadrzaj);
                                    status = m.matches();
                                    if (status) {
                                        Folder NWTiS_Poruke_Folder = store.getFolder(NWTiS_Poruke);
                                        NWTiS_Poruke_Folder.open(Folder.READ_WRITE);
                                        folder.copyMessages(folder.getMessages(i + 1, i + 1), NWTiS_Poruke_Folder);

                                        Flags deleted = new Flags(Flags.Flag.DELETED);
                                        folder.setFlags(folder.getMessages(i + 1, i + 1), deleted, true);
                                        folder.expunge();

                                        eventBr++;

                                        id = Integer.parseInt(m.group(1));
                                        String vrijemeKreiranja = m.group(2);
                                        vrijemeKreiranja = vrijemeKreiranja.replace(".", "-");
                                        int vrstaDogadaja = 0;
                                        if (m.group(4) != null) {
                                            vrstaDogadaja = Integer.parseInt(m.group(4));
                                        }
                                        sql = "SELECT * FROM uredaji where id=" + id;
                                        naredba = veza.createStatement();
                                        odgovor = naredba.executeQuery(sql);

                                        if (odgovor.next()) {
                                            id = 0;
                                            sql = "SELECT id FROM `dogadaji` ORDER BY id DESC LIMIT 1";
                                            naredba = veza.createStatement();
                                            odgovor = naredba.executeQuery(sql);
                                            while (odgovor.next()) {
                                                id = odgovor.getInt("id");
                                            }
                                            id++;
                                            sql = "INSERT INTO dogadaji (id, vrsta, vrijeme_kreiranja) VALUES(" + id + "," + vrstaDogadaja + ",'" + Timestamp.valueOf(vrijemeKreiranja) + "');";
                                            naredba = veza.createStatement();
                                            naredba.executeUpdate(sql);
                                            IoT++;
                                        } else {
                                            System.err.println("(EVENT) U bazi NE postoji zapis s tim id: " + id);
                                            pogreske++;
                                        }
                                    } else {
                                        System.err.println("Nije ispravan unos!");
                                        pogreske++;
                                        Folder NWTiS_Poruke_Folder = store.getFolder(NWTiS_OstalePoruke);
                                        NWTiS_Poruke_Folder.open(Folder.READ_WRITE);
                                        folder.copyMessages(folder.getMessages(i + 1, i + 1), NWTiS_Poruke_Folder);

                                        Flags deleted = new Flags(Flags.Flag.DELETED);
                                        folder.setFlags(folder.getMessages(i + 1, i + 1), deleted, true);
                                        folder.expunge();
                                    }
                                }
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                long trajanjeObrade = System.currentTimeMillis();
                String obradaZavrsila = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss.SSS").format(new Date());

                trajanjeObrade = trajanjeObrade - startTime;

                String sadrzaj = "Obrada započela u:" + obradaZapocela + "\n"
                        + "Obrada završila u:" + obradaZavrsila + "\n"
                        + "Trajanje obrade u ms:" + trajanjeObrade + "\n"
                        + "Broj poruka:" + porukaBr + "\n"
                        + "Broj dodanih IOT: " + IoT + "\n"
                        + "Broj mjerenih TEMP:" + tempBr + "\n"
                        + "Broj izvršenih EVENT: " + eventBr + "\n"
                        + "Broj pogrešaka:" + pogreske;

                MimeMessage message = new MimeMessage(session);

                Address[] toAddresses = InternetAddress.parse(korisnikStatistika);
                message.setRecipients(Message.RecipientType.TO, toAddresses);

                message.setSubject(predmetStatistika);
                message.setText(sadrzaj);

                Transport.send(message);

                sleep(trajanjeCiklusa * 1000 - trajanjeObrade);
            } catch (InterruptedException | MessagingException | IOException ex) {
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
    public void setSc(ServletContext sc) {
        this.sc = sc;
    }

}
