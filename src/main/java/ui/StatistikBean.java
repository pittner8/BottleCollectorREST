/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import model.Benutzer;
import service.BenutzerFacadeREST;

/**
 *
 * @author Florian
 */
@Named
@ViewScoped
public class StatistikBean implements Serializable{
    @Inject
    private BenutzerFacadeREST service;
    private Benutzer user;
    
    @PostConstruct
    public void ini(){
        // beim neuladen nicht abstürzen
        user = (Benutzer) FacesContext.getCurrentInstance().getAttributes().get("benutzer");
    }

    public Benutzer getUser() {
        return user;
    }
}
