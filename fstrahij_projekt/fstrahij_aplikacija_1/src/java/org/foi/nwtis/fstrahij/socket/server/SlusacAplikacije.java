/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.socket.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
import org.foi.nwtis.fstrahij.socket.server.PreuzmiMeteoPodatke;

/**
 * Web application lifecycle listener.
 *
 * Listener koji pokreće dretvu te inicijalizira ServletContext
 *
 * @author fstrahij
 */
@WebListener
public class SlusacAplikacije implements ServletContextListener {

    public static PreuzmiMeteoPodatke op = new PreuzmiMeteoPodatke();;

    public static ServletContext context;
    
    private ServerSustava ss;

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

        BP_Konfiguracija bp_konf = new BP_Konfiguracija(datoteka);
        context.setAttribute("BP_Konfig", bp_konf);
        System.out.println("Učitana konfiguacija");
        Konfiguracija konf = null;
        int port = 0;
        try {
            konf = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
            context.setAttribute("Konfig", konf);         
            port = Integer.parseInt(konf.dajPostavku("port"));
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }

        op.setSc(context);
        op.start();
        
        ss = new ServerSustava(port);
        ss.start();
    }

    /**
     *
     * @param sce
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (op != null) {
            op.interrupt();
        }
        if (ss != null) {
            ss.interrupt();
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
}
