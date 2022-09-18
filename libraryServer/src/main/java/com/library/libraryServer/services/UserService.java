package com.library.libraryServer.services;

import com.library.libraryServer.domain.*;
import com.library.libraryServer.domain.dto.*;
import com.library.libraryServer.domain.enums.*;
import com.library.libraryServer.exceptions.*;
import com.library.libraryServer.repository.*;
import com.library.libraryServer.security.jwt.*;
import lombok.extern.slf4j.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileService profileService;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ProfileService profileService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

        this.profileService = profileService;
    }

    public Optional<User> findByEmail(String email) {
        log.info("Request to find user with email : {}", email);

        Optional<User> user = userRepository.findByEmail(email);
        log.info("Found user : {}", user);
        return user;
    }
    public User save(User user) {
        log.info("Request to save user : {}", user);
        return userRepository.save(user);
    }

    public User create(String email, String name, String password) {
        log.debug("Request to register user with email : {}, and name : {}", email, name);
        User user = new User();

        user.setEmail(email);
        user.setName(name);

        user.setAuthority(Authority.SUBSCRIBER);
        user.setPassword(this.passwordEncoder.encode(password));
        user.setAccountStatus(AccountStatus.UNPAID);

        user = userRepository.save(user);
        return user;


    }

    public User findById(Long id) {

        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return user;
        } else {
            return null;
        }
    }

    public SubscriberDTO findOneSubscriber(Long id) {
        log.info("Request to find subscriber with id : {}", id);

        Optional<User> optionalUser = userRepository.findById(id);

        SubscriberDTO subscriberDTO = null;
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            subscriberDTO = new SubscriberDTO(user);

            // get profiles
            List<ProfileDTO> profiles = profileService.getProfilesByUser(user.getId());
            subscriberDTO.setProfiles(profiles);
        }

        return subscriberDTO;
    }


    public User getCurrentLoggedInUser() {
        log.info("Request to get currently logged in user");
        Optional<String> optionalEmail = SecurityUtils.getCurrentUseEmail();

        User user = null;

        if (optionalEmail.isPresent()) {
            String email = optionalEmail.get();
            log.info("Finding user with email : {}", email);

            Optional<User> optionalUser = findByEmail(email);

            if (optionalUser.isPresent()) {
                user = optionalUser.get();
            }
        }

        return user;
    }

    public User update(User user) {

        log.info("Request to update user with id : {}", user.getEmail());

        return userRepository.save(user);
    }

    public void updateUser(UserDTO userDTO) throws UserNotFoundException {
        log.debug("Request to update user : {}", userDTO);

        //get the user
        Optional<User> userOptional = this.userRepository.findById(userDTO.getId());


        if (userOptional.isPresent()) {
            User user = userOptional.get();
            //Update the relevant fields
            user.setAuthority(userDTO.getAuthority());
            user.setName(userDTO.getName());
            user.setAccountStatus(userDTO.getAccountStatus());
            user.setPhoneNumber(userDTO.getPhoneNumber());
            user.setPassword(userDTO.getPassword());
            user.setLastBillingDate(userDTO.getLastBillingDate());
            user.setNextBillingDate(userDTO.getNextBillingDate());
            user.setAccount(userDTO.getAccount());
            user.setEmail(userDTO.getEmail());
            user.setId(userDTO.getId());


            //Update the user info
            save(user);

        } else {
            throw new UserNotFoundException("No user found with id " + userDTO.getId());
        }


    }


}
