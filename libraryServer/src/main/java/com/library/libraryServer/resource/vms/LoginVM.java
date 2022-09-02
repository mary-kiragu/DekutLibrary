package com.library.libraryServer.resource.vms;

import com.sun.istack.*;

public class LoginVM {
    @NotNull
    private String email;
    @NotNull
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginVM{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
