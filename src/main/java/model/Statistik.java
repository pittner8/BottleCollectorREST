/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author fpittner
 */
@Entity
public class Statistik implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private double gesamt;
    private double besterTagMeter;
    private double aktuellerTagMeter;
    private double besteWocheMeter;
    private double aktuelleWocheMeter;
    private String besterTagDatum = LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
    private int besteWocheJahr;
    private int besteWocheInInt;
    
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

    public double getBesterTagMeter() {
        return besterTagMeter;
    }

    public void setBesterTagMeter(double besterTagMeter) {
        this.besterTagMeter = besterTagMeter;
    }

    public double getAktuellerTagMeter() {
        return aktuellerTagMeter;
    }

    public void setAktuellerTagMeter(double aktuellerTagMeter) {
        this.aktuellerTagMeter = aktuellerTagMeter;
    }

    public double getBesteWocheMeter() {
        return besteWocheMeter;
    }

    public void setBesteWocheMeter(double besteWocheMeter) {
        this.besteWocheMeter = besteWocheMeter;
    }

    public double getAktuelleWocheMeter() {
        return aktuelleWocheMeter;
    }

    public void setAktuelleWocheMeter(double aktuelleWocheMeter) {
        this.aktuelleWocheMeter = aktuelleWocheMeter;
    }

    public String getBesterTagDatum() {
        return besterTagDatum;
    }

    public void setBesterTagDatum(String besterTagDatum) {
        this.besterTagDatum = besterTagDatum;
    }

    public int getBesteWocheJahr() {
        return besteWocheJahr;
    }

    public void setBesteWocheJahr(int besteWocheJahr) {
        this.besteWocheJahr = besteWocheJahr;
    }

    public int getBesteWocheInInt() {
        return besteWocheInInt;
    }

    public void setBesteWocheInInt(int besteWocheInInt) {
        this.besteWocheInInt = besteWocheInInt;
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
