/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.web.slusaci;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.foi.nwtis.fstrahij.ejb.sb.MeteoIoTKlijent;
import org.foi.nwtis.fstrahij.konfiguracije.Konfiguracija;
import org.foi.nwtis.fstrahij.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.fstrahij.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.fstrahij.konfiguracije.NemaKonfiguracije;

/**
 * Web application lifecycle listener.
 *
 * Listener koji inicijalizira ServletContext i dohvaća apikey i datoteke konfiguracije
 *
 * @author fstrahij
 */
@WebListener
public class SlusacAplikacije implements ServletContextListener {

    MeteoIoTKlijent meteoIoTKlijent = lookupMeteoIoTKlijentBean();

    public static ServletContext context;

    /**
     *
     * @param sce
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        context = sce.getServletContext();
        String datoteka = context.getRealPath("/WEB-INF")
                + File.separator
                + context.getInitParameter("konfiguracija");
        System.out.println("slusac!");
        Konfiguracija konf = null;
        try {
            konf = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
            context.setAttribute("Konfig", konf);
            String apikey = konf.dajPostavku("apikey");

            meteoIoTKlijent.postaviKorisnickePodatke(apikey);
            System.out.println("apikey postavljen!" + apikey);
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Vraća ServletContext
     *
     * @return
     */
    public ServletContext getContext() {
        return context;
    }

    /**
     * Hvata iznimku
     *
     * @param sce
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Vraća putanju
     *
     * @return
     */
    private MeteoIoTKlijent lookupMeteoIoTKlijentBean() {
        try {
            Context c = new InitialContext();
            return (MeteoIoTKlijent) c.lookup("java:global/fstrahij_zadaca_4/fstrahij_zadaca_4_2/MeteoIoTKlijent!org.foi.nwtis.fstrahij.ejb.sb.MeteoIoTKlijent");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
