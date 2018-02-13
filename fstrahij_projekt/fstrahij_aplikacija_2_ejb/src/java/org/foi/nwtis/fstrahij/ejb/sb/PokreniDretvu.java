/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.ejb.sb;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import org.foi.nwtis.fstrahij.dretve.ObradaPoruka;

/**
 *
 * @author Filip
 */
@Startup
@Singleton
public class PokreniDretvu {
    public static ObradaPoruka op;
    @PostConstruct
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public void pokreniDretvu() {       
        op = new ObradaPoruka();
        op.start();
    }
    
    
}
