/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import model.Benutzer;
import model.HighScoreDTO;
import model.Login;
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
    public void create(Benutzer entity) {
        Statistik s = new Statistik();
        em.persist(s);
        entity.setStatistik(s);
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Benutzer entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Benutzer find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<Benutzer> findAll() {
        return super.findAll();
    }

    @GET
    @Path("statistik/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Statistik getBenutzerStatistik(@PathParam("id") Long id) {
        return em.find(Benutzer.class, id).getStatistik();
    }

    @PUT
    @Path("statistik/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void updateUserStatistik(@PathParam("id") Long id, Statistik stat) {
        Statistik s = find(id).getStatistik();
        long oldId = s.getId();
        s = stat;
        s.setId(oldId);
        em.merge(s);
    }

    @GET
    @Path("highscore")
    @Produces({MediaType.APPLICATION_JSON})
    public HighScoreDTO getHighscore() {
        double highscore = 0;
        Benutzer temp = new Benutzer();
        for (Benutzer b : findAll()) {
            if (b.getStatistik().getGesamt() > highscore) {
                highscore = b.getStatistik().getGesamt();
                temp = b;
            }
        }
        return new HighScoreDTO(temp.getBenutzername(), highscore);
    }

    // https://github.com/jwtk/jjwt#jws
    // FormDataParam statt FormParam bei mehreren Param
    @POST
    @Path("login")
    @Produces({MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON})
    public String login(Login login) {
        // TODO: prüfen ob die richtigen anmelde daten gesendet werden
        // TODO: anmelden und token generieren in eigene methoden auslagern
        // TODO: methode zum verifizieren des token & key in der datenbank speichern
        Benutzer user = sucheBenutzer(login.getBenutzername());
        
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String jws = Jwts.builder()
                         .setSubject(Long.toString(user.getId()))
                         .claim("username", user.getBenutzername())
                         .setIssuedAt(new Date())
                         .signWith(key)
                         .compact();
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
            return jws;
            
        } catch (JwtException e) {
            return "error";
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public Benutzer sucheBenutzer(String name) {
        return em.createNamedQuery("benutzer.findByName", Benutzer.class).setParameter("benutzername", name).getSingleResult();
    }

}
