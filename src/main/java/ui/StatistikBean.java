/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import model.Benutzer;
import service.BenutzerFacadeREST;

/**
 *
 * @author Florian
 */
@Named
@ConversationScoped
public class StatistikBean implements Serializable{
    @Inject
    private BenutzerFacadeREST service;
    private Benutzer user;
    
    @PostConstruct
    public void ini(){
        // beim neuladen nicht abstürzen
        user = (Benutzer) FacesContext.getCurrentInstance().getAttributes().get("benutzer");
        user = service.find(user.getId());
    }

    public Benutzer getUser() {
        return user;
    }
}
