/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Florian
 */
@Entity
@XmlRootElement
public class Benutzer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true, length = 30)
    private String benutzername;
    // wird später noch zum byte[] zum testen erstmal string
    private String passwortHash;
    //private byte [] salt;

    public Benutzer() {
    }

    public Benutzer(String name, String passwd) {
        this.id = 0;
        this.benutzername = name;
        this.passwortHash = passwd;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBenutzername() {
        return benutzername;
    }

    public void setBenutzername(String benutzername) {
        this.benutzername = benutzername;
    }

//    public byte[] getPasswortHash() {
//        return passwortHash;
//    }
//
//    public void setPasswortHash(byte[] passwortHash) {
//        this.passwortHash = passwortHash;
//    }

//    public byte[] getSalt() {
//        return salt;
//    }
//
//    public void setSalt(byte[] salt) {
//        this.salt = salt;
//    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Benutzer other = (Benutzer) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public String getPasswortHash() {
        return passwortHash;
    }

    public void setPasswortHash(String passwortHash) {
        this.passwortHash = passwortHash;
    }
    
    
}
