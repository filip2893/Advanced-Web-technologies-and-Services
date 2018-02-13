/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.ejb.sb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.foi.nwtis.fstrahij.ejb.eb.Uredaji;

/**
 *
 * @author Filip
 */
@Stateless
public class UredajiFacade extends AbstractFacade<Uredaji> {

    @PersistenceContext(unitName = "fstrahij_zadaca_4_1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UredajiFacade() {
        super(Uredaji.class);
    }
    
}
