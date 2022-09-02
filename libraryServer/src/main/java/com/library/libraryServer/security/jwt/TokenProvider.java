package com.library.libraryServer.security.jwt;


import com.library.libraryServer.security.jwt.*;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {
    private static final String secretKey = "9487f4b8-d71d-11ea-87d0-0242ac130003";


    private static final String AUTH = "auth";
    private final int expiryAllowanceInSeconds = 3600;


    public String createToken(String email, long expiryTime, String authority) {
        Date validity;

        validity = new Date(expiryTime * 1000);

        return Jwts.builder()
                .setSubject(email)
                .claim(AUTH, authority)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .setExpiration(validity)
                .compact();
    }


    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();




        Collection<GrantedAuthority> authorities = Arrays.stream(claims.get(AUTH).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        log.debug("Principal = {}, Claims = {}", principal, claims);

        CustomAuthentication customAuthentication = new CustomAuthentication(principal.getAuthorities(), null, principal, true, principal.getUsername());
        customAuthentication.setBearerToken(token);

        return customAuthentication;
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace", e);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
        return false;
    }
}
