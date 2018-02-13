/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.ejb.sb;

import java.io.StringReader;
import javax.ejb.Stateful;
import javax.ejb.LocalBean;
import javax.faces.context.FacesContext;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.fstrahij.ws.klijenti.KorisnikRESTClient;

/**
 *
 * @author Filip
 */
@Stateful
@LocalBean
public class AutentikacijaKorisnika {

    public int prijavaKorisnika(String korime, String lozinka) {
        KorisnikRESTClient klijent = new KorisnikRESTClient(korime);
        String dajKorisnik = klijent.getJson();
        if (dajKorisnik.isEmpty()) {
            return 1;
        }
        JsonReader reader = Json.createReader(new StringReader(dajKorisnik));
        JsonObject obj = reader.readObject();

        String loz = obj.getString("lozinka");
        if (!loz.equals(lozinka)) {
            return 2;
        }
        
        return 0;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
}
