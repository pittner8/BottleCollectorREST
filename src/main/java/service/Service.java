/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.Benutzer;
import model.JWTKey;

/**
 *
 * @author fpittner
 */
@Stateless
public class Service {

    @PersistenceContext
    private EntityManager em;

    public Benutzer sucheBenutzer(String name) {
        return em.createNamedQuery("benutzer.findByName", Benutzer.class).setParameter("benutzername", name).getSingleResult();
    }

    public Key getKey() {
        List<JWTKey> erg = em.createNamedQuery("JWTKey.findAllKeys", JWTKey.class).getResultList();
        if (erg.isEmpty()) {
            JWTKey jKey = new JWTKey(Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded());
            anlegenJWTKey(jKey);
            return Keys.hmacShaKeyFor(jKey.getPrivateKey());
        } else {
            return Keys.hmacShaKeyFor(erg.get(0).getPrivateKey());
        }
    }

    public void anlegenJWTKey(JWTKey key) {
        em.persist(key);
    }

    public void mergeBenutzer(Benutzer b) {
        em.merge(b);
    }
    
    public Benutzer findByToken(String token) throws Exception {
        return em.createNamedQuery("Benutzer.findByToken", Benutzer.class).setParameter("token", token).getSingleResult();
    }
}
