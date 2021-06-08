/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author fpittner
 */
@Entity
@XmlRootElement
public class Statistik implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private double gesamt;
    private double besterTag;
    private double aktuellerTag;
    private double besteWoche;
    private double aktuelleWoche;
    
    public Statistik (){
        
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getGesamt() {
        return gesamt;
    }

    public void setGesamt(double gesamt) {
        this.gesamt = gesamt;
    }

    public double getBesterTag() {
        return besterTag;
    }

    public void setBesterTag(double besterTag) {
        this.besterTag = besterTag;
    }

    public double getAktuellerTag() {
        return aktuellerTag;
    }

    public void setAktuellerTag(double aktuellerTag) {
        this.aktuellerTag = aktuellerTag;
    }

    public double getBesteWoche() {
        return besteWoche;
    }

    public void setBesteWoche(double besteWoche) {
        this.besteWoche = besteWoche;
    }

    public double getAktuelleWoche() {
        return aktuelleWoche;
    }

    public void setAktuelleWoche(double aktuelleWoche) {
        this.aktuelleWoche = aktuelleWoche;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Statistik other = (Statistik) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
}
