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
import java.util.Arrays;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.validation.constraints.Size;
import model.Benutzer;
import service.BenutzerFacadeREST;
import service.Service;

/**
 *
 * @author Florian
 */
@Named
@SessionScoped
public class BenutzerBean implements Serializable{
    
    @Inject
    private Service service;
    @Inject
    private BenutzerFacadeREST benutzerService;
    @Size(min = 5, message = "Passwort muss mindestens 5 Zeichen lang sein")
    private String passwort;
    private String passwort2;
    private Benutzer user = new Benutzer();
    private boolean benutzerExistiertNicht = false;
    private boolean benutzerExistiert = false;
    
    public String anmelden() {
        try {
            Benutzer dbUser = service.sucheBenutzer(user.getBenutzername());
            user.setPasswortHash(hashWithSalt(passwort, dbUser.getSalt()));
            if (Arrays.equals(dbUser.getPasswortHash(), user.getPasswortHash())) {
                FacesContext.getCurrentInstance().getAttributes().put("benutzer", dbUser);
                return "statistik";
            }
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NullPointerException ex) {
            FacesContext.getCurrentInstance().addMessage("err", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Anmelden fehlgeschlagen. Benutzername oder Passwort falsch!", ""));
        } catch (NoResultException | EJBException ex){
            FacesContext.getCurrentInstance().addMessage("err", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Anmelden fehlgeschlagen. Benutzer gibt es nicht! Stattdessen registrieren", ""));
            benutzerExistiertNicht = true;
        }
        return "anmeldung";
    }
    
    public String registrieren() {
        if(!passwort.equals(passwort2)){
            FacesContext.getCurrentInstance().addMessage("err", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwörter stimmen nicht überein", ""));
            return "registrieren";
        }
        
        byte[] salt = generateSalt(passwort.length());

        try {
            user.setPasswortHash(hashWithSalt(passwort, salt));
            benutzerService.create(user);
            FacesContext.getCurrentInstance().getAttributes().put("benutzer", service.sucheBenutzer(user.getBenutzername()));
            return "statistik";
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | EJBException e) {
            FacesContext.getCurrentInstance().addMessage("err", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registrierung fehlgeschlagen. Benutzer gibt es bereits!", ""));
            benutzerExistiert = true;
        }
        return "registrieren";
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

    public boolean isBenutzerExistiertNicht() {
        return benutzerExistiertNicht;
    }

    public boolean isBenutzerExistiert() {
        return benutzerExistiert;
    }

    public String getPasswort2() {
        return passwort2;
    }

    public void setPasswort2(String passwort2) {
        this.passwort2 = passwort2;
    }
}
