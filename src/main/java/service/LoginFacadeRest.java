/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import model.Benutzer;
import model.Login;

/**
 *
 * @author fpittner
 */
@Stateless
@Path("account")
public class LoginFacadeRest {
    @Inject
    Service service;
    
    // https://github.com/jwtk/jjwt#jws
    @POST
    @Path("login")
    @Produces({MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON})
    public String login(Login login) {
        if(!validiereBenutzer(login)) return "Ungültiger Benutzer oder passwort.";
        Key key = service.getKey();
        Benutzer user = service.sucheBenutzer(login.getBenutzername());
        if(user.getToken() != null){
            // prüft ob der Token bereits abgelaufen ist.
            // Wenn er nicht abgelaufen ist wird der alte Token returnt
            try {
                Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(user.getToken());
                return user.getToken();
            } catch (JwtException e){} // absichtlich leer
        }
        
        try {
            String jws = generateToken(user, key);
            user.setToken(jws);
            service.mergeBenutzer(user);
            return jws;
        } catch (JwtException e) {
            return "Fehler beim login.";
        }
    }
    
    private String generateToken(Benutzer user, Key key) throws JwtException{
        String jws = Jwts.builder()
                         .setSubject(Long.toString(user.getId()))
                         .claim("username", user.getBenutzername())
                         .setIssuedAt(new Date())
                         .setExpiration(new Date(new Date().getTime()+(20*1000))) // setzt die Gültigkeit des Token auf 4 Stunden (4*60*60*1*1000)
                         .signWith(key)
                         .compact();
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
        return jws;
    }
    
    private boolean validiereBenutzer(Login loginUser){
        try {
            Benutzer dbUser = service.sucheBenutzer(loginUser.getBenutzername());
            Benutzer user = new Benutzer();
            user.setBenutzername(loginUser.getBenutzername());
            user.setPasswortHash(hashWithSalt(loginUser.getPasswort(), dbUser.getSalt()));
            return Arrays.equals(dbUser.getPasswortHash(), user.getPasswortHash());
        } catch (NullPointerException | NoResultException | EJBException | NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            return false;
        }
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
}
