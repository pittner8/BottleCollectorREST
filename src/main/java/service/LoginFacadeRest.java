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
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import model.Benutzer;
import model.JWTKey;
import model.Login;

/**
 *
 * @author fpittner
 */
@Stateless
@Path("account")
public class LoginFacadeRest {
    @Inject
    BenutzerFacadeREST service;
    
    // https://github.com/jwtk/jjwt#jws
    // FormDataParam statt FormParam bei mehreren Param
    @POST
    @Path("login")
    @Produces({MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON})
    public String login(Login login) {
        // TODO: anmelden und token generieren in eigene methoden auslagern
        // TODO: methode zum verifizieren des token & key in der datenbank speichern
        if(!validiereBenutzer(login)) return "error!!!!!!!";
        Benutzer user = service.sucheBenutzer(login.getBenutzername());
        
        JWTKey byteKey;
        if(service.getJWTKey().isEmpty()){
            service.anlegenJWTKey(new JWTKey(Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded()));
        }
        byteKey = service.getJWTKey().get(0);
        Key key = Keys.hmacShaKeyFor(byteKey.getPrivateKey());
        String jws = generateToken(login);
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
            return jws;
            
        } catch (JwtException e) {
            return "error";
        }
    }
    
    private String generateToken(Login login){
        Benutzer user = service.sucheBenutzer(login.getBenutzername());
        
        Key key = Keys.hmacShaKeyFor(service.getJWTKey().get(0).getPrivateKey());
        String jws = Jwts.builder()
                         .setSubject(Long.toString(user.getId()))
                         .claim("username", user.getBenutzername())
                         .setIssuedAt(new Date())
                         .signWith(key)
                         .compact();
        return jws;
    }
    
    private boolean validiereBenutzer(Login loginUser){
        try {
            Benutzer dbUser = service.sucheBenutzer(loginUser.getBenutzername());
            Benutzer user = new Benutzer();
            user.setBenutzername(loginUser.getBenutzername());
            user.setPasswortHash(hashWithSalt(loginUser.getPasswort(), dbUser.getSalt()));
            if (Arrays.equals(dbUser.getPasswortHash(), user.getPasswortHash())) {
                return true;
            }else{
                return false;
            }
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
