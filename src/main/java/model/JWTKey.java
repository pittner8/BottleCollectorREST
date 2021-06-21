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
import javax.persistence.NamedQuery;

/**
 *
 * @author fpittner
 */
@Entity
@NamedQuery(name = "JWTKey.findAllKeys", query = "SELECT k FROM JWTKey k")
public class JWTKey implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private byte[] privateKey;

    public JWTKey(byte[] key) {
        this.id = 0;
        this.privateKey = key;
    }
    
    public JWTKey() {
    }

    public long getId() {
        return id;
    }

    public byte[] getPrivateKey() {
        return privateKey;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPrivateKey(byte[] key) {
        this.privateKey = key;
    }
}
