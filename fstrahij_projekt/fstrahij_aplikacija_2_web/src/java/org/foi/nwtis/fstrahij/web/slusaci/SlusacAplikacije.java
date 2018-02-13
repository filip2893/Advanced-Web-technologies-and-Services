/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.web.slusaci;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.foi.nwtis.fstrahij.konfiguracije.Konfiguracija;
import org.foi.nwtis.fstrahij.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.fstrahij.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.fstrahij.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.fstrahij.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.fstrahij.dretve.ObradaPoruka;
import org.foi.nwtis.fstrahij.ejb.sb.PokreniDretvu;

/**
 * Slušač koji starta dretve za obradu te postavlja kontext
 *
 * Web application lifecycle listener.
 *
 * @author fstrahij
 */
@WebListener
public class SlusacAplikacije implements ServletContextListener {

    /**
     * Služi za dohvat podataka konfiguracije koji su potrebni za rad
     *
     * @param sce
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        String datoteka = context.getRealPath("/WEB-INF") + File.separator
                + context.getInitParameter("konfiguracija");

        BP_Konfiguracija bp_konf = new BP_Konfiguracija(datoteka);
        context.setAttribute("BP_Konfig", bp_konf);

        try {
            Konfiguracija konfg = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
            context.setAttribute("Mail_Konfig", konfg);
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }

        PokreniDretvu.op.setSc(context);

    }

    /**
     * Prekida rad dretve
     *
     * @param sce
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if ( PokreniDretvu.op != null) {
             PokreniDretvu.op.interrupt();
        }
    }
}
