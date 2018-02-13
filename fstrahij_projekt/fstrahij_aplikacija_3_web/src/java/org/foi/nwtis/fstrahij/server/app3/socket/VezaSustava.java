/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.server.app3.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Služi za uspostavljanje veze klijenta s serverom te primanje i slanje poruka
 *
 * @author Filip
 */
public class VezaSustava {
    public static String odgovor;

    public void posaljiNaSocketu(String nazivServera, int port, String zahtjev) {
        InputStream is = null;
        OutputStream os = null;
        Socket s = null;

        try {
            s = new Socket(nazivServera, port);
            is = s.getInputStream();
            os = s.getOutputStream();

            //String zahtjev = "USER pero; ADD 100;";
            os.write(zahtjev.getBytes());
            os.flush();
            s.shutdownOutput();

            StringBuffer sb = new StringBuffer();
            while (true) {
                int znak = is.read();
                if (znak == -1) {
                    break;
                }
                sb.append((char) znak);
            }
            odgovor = sb.toString();
            System.out.println("Primljeni  odgovor: " + sb);
        } catch (IOException ex) {
            Logger.getLogger(VezaSustava.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
                s.close();
            } catch (IOException ex) {
                Logger.getLogger(VezaSustava.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }
}
