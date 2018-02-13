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
@Table(name = "UREDAJI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Uredaji.findAll", query = "SELECT u FROM Uredaji u")
    , @NamedQuery(name = "Uredaji.findById", query = "SELECT u FROM Uredaji u WHERE u.id = :id")
    , @NamedQuery(name = "Uredaji.findByNaziv", query = "SELECT u FROM Uredaji u WHERE u.naziv = :naziv")
    , @NamedQuery(name = "Uredaji.findByLatitude", query = "SELECT u FROM Uredaji u WHERE u.latitude = :latitude")
    , @NamedQuery(name = "Uredaji.findByLongitude", query = "SELECT u FROM Uredaji u WHERE u.longitude = :longitude")
    , @NamedQuery(name = "Uredaji.findByStatus", query = "SELECT u FROM Uredaji u WHERE u.status = :status")
    , @NamedQuery(name = "Uredaji.findByVrijemePromjene", query = "SELECT u FROM Uredaji u WHERE u.vrijemePromjene = :vrijemePromjene")
    , @NamedQuery(name = "Uredaji.findByVrijemeKreiranja", query = "SELECT u FROM Uredaji u WHERE u.vrijemeKreiranja = :vrijemeKreiranja")})
public class Uredaji implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
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

    public Uredaji() {
    }

    public Uredaji(Integer id) {
        this.id = id;
    }

    public Uredaji(Integer id, String naziv, float latitude, float longitude, int status, Date vrijemePromjene, Date vrijemeKreiranja) {
        this.id = id;
        this.naziv = naziv;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.vrijemePromjene = vrijemePromjene;
        this.vrijemeKreiranja = vrijemeKreiranja;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Uredaji)) {
            return false;
        }
        Uredaji other = (Uredaji) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.foi.nwtis.fstrahij.ejb.eb.Uredaji[ id=" + id + " ]";
    }
    
}
