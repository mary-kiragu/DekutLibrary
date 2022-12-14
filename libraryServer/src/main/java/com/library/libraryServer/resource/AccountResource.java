package com.library.libraryServer.resource;

import com.library.libraryServer.domain.*;
import com.library.libraryServer.domain.dto.*;
import com.library.libraryServer.exceptions.*;
import com.library.libraryServer.resource.vms.*;
import com.library.libraryServer.security.jwt.*;
import com.library.libraryServer.services.*;
import com.library.libraryServer.services.util.*;
import com.library.libraryServer.util.*;
import lombok.extern.slf4j.*;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.web.bind.annotation.*;

import javax.management.*;
import javax.validation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
@Slf4j
public class AccountResource {
    private final TokenProvider tokenProvider;
    private final LibraryAuthenticationManager libraryAuthenticationManager;
    private final UserService userService;

    private final MailService mailService;

    public AccountResource(TokenProvider tokenProvider, LibraryAuthenticationManager libraryAuthenticationManager, UserService userService, MailService mailService) {
        this.tokenProvider = tokenProvider;
        this.libraryAuthenticationManager =libraryAuthenticationManager;
        this.userService=userService;
        this.mailService = mailService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseVM> register(@RequestBody RegisterUserVM registerUserVM) {
        log.debug("REST request to register user : {}", registerUserVM);

        String normalizedEmail = GmailUtil.normalizeEmail(registerUserVM.getEmail());
        Optional<User> userOptional = userService.findByEmail(normalizedEmail);

        if (userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RegisterResponseVM(400, "email address already used"));
        }

        try {
            this.userService.create(normalizedEmail, registerUserVM.getName(),registerUserVM.getPassword());
            return ResponseEntity.ok().body(new RegisterResponseVM(200, "Account successfully created"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RegisterResponseVM(500, "Error creating user"));
        }
    }



    @PostMapping("/authenticate")
    public ResponseEntity<JWTResponseVM> authorize(@Valid @RequestBody LoginVM loginVM) {
        String normalizedEmail = GmailUtil.normalizeEmail(loginVM.getEmail());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                normalizedEmail,
                loginVM.getPassword()
        );

        try {
            User user = libraryAuthenticationManager.authenticateAndReturnUser(authenticationToken);

            long expiryTime = 86400 + (System.currentTimeMillis() / 1000);
            String jwt = tokenProvider.createToken(normalizedEmail,expiryTime, user.getAuthority().toString());
            return ResponseEntity.ok().body(new JWTResponseVM(jwt, user.getAuthority().name()));
        }  catch (AuthenticationException exception) {

            log.warn("Error authenticating user", exception);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JWTResponseVM(401, "invalid username or password"));
        } catch (UserNotFoundException e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JWTResponseVM(404, "User not found"));

        }
    }

    @PostMapping(path = "/account/reset-password/init")
    public ResponseEntity<CustomResponseVM> requestPasswordReset(@RequestBody String mail) {
        Optional<User> user = userService.requestPasswordReset(mail);
        if (user.isPresent()) {
            User fetchedUser = user.get();

            String emailSubject = "Reset Password";
            String body = TemplateUtil.generatePasswordReset(fetchedUser.getName(), fetchedUser.getResetKey(),mail);
            NotifyEmailDTO emailDTO = new NotifyEmailDTO(fetchedUser.getEmail(), emailSubject, body, true,
                    false);
            mailService.sendEmail(emailDTO);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new CustomResponseVM(202, "Check your email for instructions on how to reset password"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponseVM(400, "User with specified email does not exist"));
        }
    }
}
