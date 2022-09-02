package com.library.libraryServer.security.jwt;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAuthentication implements Authentication {
    private final Collection<GrantedAuthority> authorities;

    private final String details;

    private final Object principal;

    private final boolean authenticated;

    private final String name;



    private String bearerToken;

    public CustomAuthentication(Collection<GrantedAuthority> authorities, String details, Object principal, boolean authenticated, String name) {
        this.authorities = authorities;
        this.details = details;
        this.principal = principal;
        this.authenticated = authenticated;
        this.name = name;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return this.details;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return this.name;
    }



    public String getBearerToken() {
        return bearerToken;
    }

    public void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
    }
}
