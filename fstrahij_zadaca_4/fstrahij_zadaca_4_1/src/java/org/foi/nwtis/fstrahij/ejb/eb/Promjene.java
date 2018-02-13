/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.fstrahij.ejb.eb;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Filip
 */
@Entity
@Table(name = "PROMJENE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Promjene.findAll", query = "SELECT p FROM Promjene p")
    , @NamedQuery(name = "Promjene.findByPid", query = "SELECT p FROM Promjene p WHERE p.pid = :pid")
    , @NamedQuery(name = "Promjene.findById", query = "SELECT p FROM Promjene p WHERE p.id = :id")
    , @NamedQuery(name = "Promjene.findByNaziv", query = "SELECT p FROM Promjene p WHERE p.naziv = :naziv")
    , @NamedQuery(name = "Promjene.findByLatitude", query = "SELECT p FROM Promjene p WHERE p.latitude = :latitude")
    , @NamedQuery(name = "Promjene.findByLongitude", query = "SELECT p FROM Promjene p WHERE p.longitude = :longitude")
    , @NamedQuery(name = "Promjene.findByStatus", query = "SELECT p FROM Promjene p WHERE p.status = :status")
    , @NamedQuery(name = "Promjene.findByVrijemePromjene", query = "SELECT p FROM Promjene p WHERE p.vrijemePromjene = :vrijemePromjene")
    , @NamedQuery(name = "Promjene.findByVrijemeKreiranja", query = "SELECT p FROM Promjene p WHERE p.vrijemeKreiranja = :vrijemeKreiranja")})
public class Promjene implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PID")
    private Integer pid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private int id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "NAZIV")
    private String naziv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LATITUDE")
    private float latitude;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LONGITUDE")
    private float longitude;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STATUS")
    private int status;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VRIJEME_PROMJENE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vrijemePromjene;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VRIJEME_KREIRANJA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vrijemeKreiranja;

    public Promjene() {
    }

    public Promjene(Integer pid) {
        this.pid = pid;
    }

    public Promjene(Integer pid, int id, String naziv, float latitude, float longitude, int status, Date vrijemePromjene, Date vrijemeKreiranja) {
        this.pid = pid;
        this.id = id;
        this.naziv = naziv;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.vrijemePromjene = vrijemePromjene;
        this.vrijemeKreiranja = vrijemeKreiranja;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getVrijemePromjene() {
        return vrijemePromjene;
    }

    public void setVrijemePromjene(Date vrijemePromjene) {
        this.vrijemePromjene = vrijemePromjene;
    }

    public Date getVrijemeKreiranja() {
        return vrijemeKreiranja;
    }

    public void setVrijemeKreiranja(Date vrijemeKreiranja) {
        this.vrijemeKreiranja = vrijemeKreiranja;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pid != null ? pid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Promjene)) {
            return false;
        }
        Promjene other = (Promjene) object;
        if ((this.pid == null && other.pid != null) || (this.pid != null && !this.pid.equals(other.pid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.foi.nwtis.fstrahij.ejb.eb.Promjene[ pid=" + pid + " ]";
    }
    
}
