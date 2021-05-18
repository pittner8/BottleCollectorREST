/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Size;
import model.Benutzer;
import service.BenutzerFacadeREST;

/**
 *
 * @author Florian
 */
@Named
@SessionScoped
public class BenutzerBean implements Serializable{
    
    @Inject
    private BenutzerFacadeREST service;
    @Size(min = 5)
    private String passwort;
    private Benutzer user = new Benutzer();
    
    public void regestrieren() {
        byte[] salt = generateSalt(passwort.length());

        try {
            user.setPasswortHash(hashWithSalt(passwort, salt));
            service.create(user);
            user = new Benutzer();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erfolgreich angemeldet", " "));
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | EJBException e) {
            FacesContext.getCurrentInstance().addMessage("err", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Regestrierung fehlgeschlagen.", " Benutzer gibt es bereits!"));
            user = new Benutzer();
        }
    }

    private byte[] generateSalt(int length) {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[length];
        random.nextBytes(bytes);
        user.setSalt(bytes);
        return bytes;
    }

    private byte[] hashWithSalt(String password, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] hash = null;
        byte[] bytesOfMessage = password.getBytes("UTF-8");
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-512");
        md.reset();
        md.update(salt);
        md.update(bytesOfMessage);
        hash = md.digest();
        return hash;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public Benutzer getUser() {
        return user;
    }

    public void setUser(Benutzer user) {
        this.user = user;
    }
    
}
