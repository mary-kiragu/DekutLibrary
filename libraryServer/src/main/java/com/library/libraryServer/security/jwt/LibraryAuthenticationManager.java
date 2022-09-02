package com.library.libraryServer.security.jwt;

import com.library.libraryServer.domain.*;
import com.library.libraryServer.exceptions.*;
import com.library.libraryServer.services.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;

import java.util.*;

@Slf4j
@Component
public class LibraryAuthenticationManager implements AuthenticationManager {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public LibraryAuthenticationManager(PasswordEncoder passwordEncoder, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @SneakyThrows
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();

        Optional<User> optionalUser = userService.findByEmail(username);

        // check if user exists
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("Username not registered");
        }

        User user = optionalUser.get();

        if (password.isEmpty() || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid Credentials");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new LibraryAuthority(user.getAuthority()));


        return new CustomAuthentication(grantedAuthorities, null, username, true, user.getName());
    }


    public User authenticateAndReturnUser(Authentication authentication) throws AuthenticationException,UserNotFoundException {

        String username = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();

        Optional<User> optionalUser = userService.findByEmail(username);

        // check if user exists
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("Username not registered");
        }

        User user = optionalUser.get();



        if (password.isEmpty() || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid Credentials");
        }


        return user;
    }
}
