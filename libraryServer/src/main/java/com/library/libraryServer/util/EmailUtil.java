package com.library.libraryServer.util;

public class EmailUtil {
    public static String getEmailDomain(String emailAddress) {
        if (emailAddress != null && emailAddress.contains("@")) {
            return emailAddress.split("@")[1];
        }
        return null;
    }
}
