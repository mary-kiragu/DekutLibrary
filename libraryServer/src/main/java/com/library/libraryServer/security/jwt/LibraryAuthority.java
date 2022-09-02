package com.library.libraryServer.security.jwt;

import com.library.libraryServer.domain.enums.*;
import org.springframework.security.core.*;

public class LibraryAuthority implements GrantedAuthority {
    private String authority;

    public LibraryAuthority(Authority authority) {
        this.authority = authority.toString();
    }



    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        return "LibraryAuthority{" +
                "authority='" + authority + '\'' +
                '}';
    }
}
