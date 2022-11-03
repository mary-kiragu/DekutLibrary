package com.library.libraryServer.services;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.*;
import com.library.libraryServer.config.*;
import com.library.libraryServer.domain.*;
import com.library.libraryServer.domain.dto.*;
import com.library.libraryServer.domain.enums.*;
import com.library.libraryServer.exceptions.*;
import com.library.libraryServer.mapper.*;
import com.library.libraryServer.repository.*;
import com.library.libraryServer.security.jwt.*;
import com.library.libraryServer.util.*;
import lombok.extern.slf4j.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;

import javax.management.*;
import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import java.util.*;

@Service
@Slf4j

public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileService profileService;

    private final MailService mailService;

    private final UserMapper userMapper;

    private final MpesaConfiguration mpesaConfiguration;
    //ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());




    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ProfileService profileService, MailService mailService, UserMapper userMapper, MpesaConfiguration mpesaConfiguration) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

        this.profileService = profileService;
        this.mailService = mailService;
        this.userMapper = userMapper;
        this.mpesaConfiguration = mpesaConfiguration;
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
    public List<User> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        log.info("This is a list of all users found:{} ", allUsers);
        return allUsers;
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
        Optional<String> optionalEmail = SecurityUtils.getCurrentUserLogin2();
        log.info("Request to get currently logged in user {}",optionalEmail);


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


    @Scheduled(cron = "0 0 7 * * *")

    public void sendDuedateReminder() {
        log.info("------------------------- ");

        log.info("Sending due date reminders");


        LocalDate now = LocalDate.now();

        short paymentReminder = mpesaConfiguration.getPaymentReminder();

        List<User> users = getAllUsers();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (User user : users) {
            if (user.getEmail() != null && user.getLastBillingDate()!=null) {

               // long daysBetween = Math.abs(ChronoUnit.DAYS.between(LocalDate.parse(user.getNextBillingDate(), formatter), now));
                long daysBetween = Math.abs(ChronoUnit.DAYS.between(user.getNextBillingDate(), now));
                log.info("Day difference : {}", daysBetween);
                if (daysBetween == paymentReminder) {
                    UserDTO userDTO = userMapper.toDTO(user);
                    log.info("Day difference dto : {}", user);
                    // send duedate reminder
                    log.info("About to send ");
                    String body = TemplateUtil.generatePaymentReminder(paymentReminder, userDTO);
                    NotifyEmailDTO notifyEmailDTO = new NotifyEmailDTO(user.getEmail(),
                            "Book Due date Reminder", body, true, false);

                    log.info("about to send email:{}", notifyEmailDTO);

                    mailService.sendEmail(notifyEmailDTO);
                }
            }
            else{
                log.info("No email or lastBillingDate");
                continue;
            }
        }





    }
    @Scheduled(cron = "0 0 7 * * *")
    public void CheckSubscription(){
        LocalDate now = LocalDate.now();
        List<User> users = getAllUsers();
        for (User user : users) {
            if (user.getEmail() != null && user.getLastBillingDate()!=null) {
                if(now.isAfter(user.getNextBillingDate()) && user.getAccountStatus()==AccountStatus.PAID){
                    user.setAccountStatus(AccountStatus.UNPAID);
                }


            }
            else{
                log.info("No email or lastBillingDate");
                continue;
            }

    }




}

}
