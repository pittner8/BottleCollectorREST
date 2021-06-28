/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import annotation.JWTNeeded;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import model.Benutzer;
import model.HighScoreDTO;
import model.Statistik;

/**
 *
 * @author Florian
 */
@Stateless
@Path("benutzer")
public class BenutzerFacadeREST extends AbstractFacade<Benutzer> {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    public BenutzerFacadeREST() {
        super(Benutzer.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    @JWTNeeded
    public void create(Benutzer entity) {
        Statistik s = new Statistik();
        em.persist(s);
        entity.setStatistik(s);
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @JWTNeeded
    public void edit(@PathParam("id") Long id, Benutzer entity) {
        super.edit(entity);
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @JWTNeeded
    public Benutzer find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    @JWTNeeded
    public List<Benutzer> findAll() {
        return super.findAll();
    }

    @GET
    @Path("statistik/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @JWTNeeded
    public Statistik getBenutzerStatistik(@PathParam("id") Long id) {               // noch nicht fertig implementiert
        return em.find(Benutzer.class, id).getStatistik();                          // methode ist nur zu testzwecken da
    }

    @PUT
    @Path("statistik/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @JWTNeeded
    public void updateUserStatistik(@PathParam("id") Long id, Statistik stat) {     // noch nicht fertig implementiert
        Statistik s = find(id).getStatistik();                                      // methode ist nur zu testzwecken da
        long oldId = s.getId();
        s = stat;
        s.setId(oldId);
        em.merge(s);
    }

    @GET
    @Path("highscore")
    @Produces({MediaType.APPLICATION_JSON})
    @JWTNeeded
    public HighScoreDTO getHighscore() {
        double highscore = -1;
        Benutzer temp = new Benutzer();
        for (Benutzer b : findAll()) {
            if (b.getStatistik().getGesamt() > highscore) {
                highscore = b.getStatistik().getGesamt();
                temp = b;
            }
        }
        return new HighScoreDTO(temp.getBenutzername(), highscore);
    }

    @Override

    protected EntityManager getEntityManager() {
        return em;
    }
    
}
