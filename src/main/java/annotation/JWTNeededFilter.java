/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package annotation;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.io.IOException;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import service.BenutzerFacadeREST;

/**
 *
 * @author fpittner
 */
@Provider
@JWTNeeded
@Priority(Priorities.AUTHENTICATION)
public class JWTNeededFilter implements ContainerRequestFilter {

    @Inject
    private BenutzerFacadeREST service;

    @Override
    public void filter(ContainerRequestContext crc) throws IOException {
        try {
            String token = crc.getHeaderString(HttpHeaders.AUTHORIZATION);
            Jwts.parserBuilder().setSigningKey(service.getKey()).build().parseClaimsJws(token);
        } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | SignatureException | IllegalArgumentException e) {
            crc.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

}
