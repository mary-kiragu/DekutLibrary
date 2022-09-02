package com.library.libraryServer.security.jwt;

import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.*;

import java.util.*;

public class SecurityUtils {
    public SecurityUtils() {

    }
    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(authentication -> {
                    if(authentication instanceof CustomAuthentication){
                        return authentication.getName();
                    }
                  
                    return null;
                });
    }
    public static Optional<String> getCurrentUseEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Optional.ofNullable(authentication.getName());
    }
    public static Optional<String> getCurrentUserLogin2() {
        SecurityContext securityContext = SecurityContextHolder.getContext();

        if (securityContext.getAuthentication() != null) {
            return Optional.ofNullable(securityContext.getAuthentication().getName());
        }
        return Optional.empty();
    }
}
