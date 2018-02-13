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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.context.FacesContext;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.fstrahij.konfiguracije.Konfiguracija;
import org.foi.nwtis.fstrahij.web.podaci.Korisnik;
import org.foi.nwtis.fstrahij.ws.klijenti.KorisnikRESTClient;
import org.foi.nwtis.fstrahij.ws.klijenti.KorisnikRESTClientContainer;

/**
 *
 * @author Filip
 */
@Named(value = "pogled_1")
@SessionScoped
public class Pogled_1 implements Serializable {

    private String korime, ime, prezime, lozinka, email, greske, korimeNovo;
    private int brStranica = 0;

    FacesContext facesContext = FacesContext.getCurrentInstance();
    HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);

    private List<Korisnik> korisnici = new ArrayList<>();

    /**
     * Creates a new instance of Pogled_1
     */
    public Pogled_1() {
        ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        Konfiguracija konfig = (Konfiguracija) context.getAttribute("Mail_Konfig");
        
        this.brStranica = Integer.parseInt(konfig.dajPostavku("pogled1.table.rows"));
    }

    public String getKorime() {
        return korime;
    }

    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public String getLozinka() {
        return lozinka;
    }

    public String getEmail() {
        return email;
    }

    public void setKorime(String korime) {
        this.korime = korime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGreske() {
        return greske;
    }

    public void setGreske(String greske) {
        this.greske = greske;
    }

    public String getKorimeNovo() {
        return korimeNovo;
    }

    public void setKorimeNovo(String korimeNovo) {
        this.korimeNovo = korimeNovo;
    }

    public List<Korisnik> getKorisnici() {
        return korisnici;
    }

    public void setKorisnici(List<Korisnik> korisnici) {
        this.korisnici = korisnici;
    }

    public int getBrStranica() {
        return brStranica;
    }

    public void setBrStranica(int brStranica) {
        this.brStranica = brStranica;
    }    

    public void preuzmiPodatkeOKorisniku() {
        korime = session.getAttribute("korime").toString();
        KorisnikRESTClient klijent = new KorisnikRESTClient(korime);
        System.out.println("korime pp: " + korime);

        JsonReader reader = Json.createReader(new StringReader(klijent.getJson()));
        if (!reader.toString().isEmpty()) {
            JsonObject obj = reader.readObject();

            korimeNovo = obj.getString("korisnicno_ime");
            ime = obj.getString("ime");
            prezime = obj.getString("prezime");
            lozinka = obj.getString("lozinka");
            email = obj.getString("email");
        }
        klijent.close();
    }

    public void azurirajKorisnika() {
        System.out.println("lozinka: " + lozinka);
        System.out.println("ime: " + ime);
        System.out.println("prezime: " + prezime);
        System.out.println("korime: " + korime);
        System.out.println("email: " + email);
        if (!korimeNovo.isEmpty()
                && !ime.isEmpty()
                && !prezime.isEmpty()
                && !lozinka.isEmpty()
                && !email.isEmpty()) {

            String regEmail = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            Pattern pattern = Pattern.compile(regEmail);
            Matcher m = pattern.matcher(email.trim());
            boolean status = m.matches();
            if (!status) {
                greske = "Neispravan unos emaila";
                return;
            }
            KorisnikRESTClient klijent = new KorisnikRESTClient(korimeNovo);
            if (!korime.equals(korimeNovo) && !klijent.getJson().isEmpty()) {
                greske = "Korisničko ime već postoji";
                return;
            }
            klijent = new KorisnikRESTClient(korime);
            

            session.setAttribute("korime", korimeNovo);
            String odgovor = klijent.putJson("{\"korime\":\"" + korime + "\""
                    + ",\"korimeNovo\":\"" + korimeNovo + "\""
                    + ",\"lozinka\":\"" + lozinka + "\""
                    + ",\"ime\":\"" + ime + "\""
                    + ",\"prezime\":\"" + prezime + "\""
                    + ",\"email\":\"" + email + "\""
                    + ",\"vrsta\":\"" + "update" + "\"}");
            if (odgovor.equals("1")) {
                greske = "uspjesno izmjenjeno";
            }else{
                greske = "nije uspjesno izmjenjeno";
            }
            klijent.close();

            preuzmiPodatkeOKorisniku();
            /*
                {"id":1,
  "korime":"msin",
   "lozinka":"sin223",
   "ime":"Marko",
   "prezime":"Sin",
   "vrsta":"update"}
             */
        } else {
            greske = "treba popuniti sva polja";
        }

    }

    public void dajSveKorisnike() {
        KorisnikRESTClientContainer klijent = new KorisnikRESTClientContainer();
        String sviKorisnici = klijent.getJson();

        JsonReader reader = Json.createReader(new StringReader(sviKorisnici));

        JsonArray jsonProg = reader.readArray();
        korisnici.clear();
        for (int i = 0; i < jsonProg.size(); i++) {
            JsonObject lista = jsonProg.getJsonObject(i);
            Korisnik korisnik = new Korisnik();

            korisnik.setKorime(lista.getString("korisnicno_ime"));
            korisnik.setLozinka(lista.getString("lozinka"));
            korisnik.setIme(lista.getString("ime"));
            korisnik.setPrezime(lista.getString("prezime"));
            korisnik.setEmail(lista.getString("email"));
            korisnici.add(korisnik);
        }
    }
}
