package com.library.libraryServer.services;

import com.library.libraryServer.domain.*;
import com.library.libraryServer.domain.enums.*;
import com.library.libraryServer.repository.*;
import lombok.extern.slf4j.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    public Optional<User> findByEmail(String email) {
        log.info("Request to find user with email : {}", email);

        Optional<User> user = userRepository.findByEmail(email);
        log.info("Found user : {}", user);
        return user;
    }

    public User create(String email, String name, String password) {
        log.debug("Request to register user with email : {}, and name : {}", email, name);
        User user = new User();

        user.setEmail(email);
        user.setName(name);

        user.setAuthority(Authority.SUBSCRIBER);
        user.setPassword(this.passwordEncoder.encode(password));

        user = userRepository.save(user);
        return user;


    }
}
