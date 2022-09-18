package com.library.libraryServer.resource;

import com.library.libraryServer.domain.User;
import com.library.libraryServer.domain.dto.*;
import com.library.libraryServer.exceptions.*;
import com.library.libraryServer.security.jwt.*;
import com.library.libraryServer.services.*;
import lombok.extern.slf4j.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserResource {
    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

        @GetMapping("/user/profile")
    public com.library.libraryServer.domain.User getProfile() {
        log.info("REST request to get current user profile");

        String email = SecurityUtils.getCurrentUseEmail().orElseThrow();

        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Invalid email address : " + email);
        }

        return userOptional.get();
    }

//    @PutMapping("/user")
//    public void updateUser(@RequestBody User user) {
//        log.info("REST request to update user : {}", user);
//        userService.update(user);
//    }
@PutMapping("/user")
public void updateUser(@RequestBody UserDTO userDTO) throws UserNotFoundException {
    log.info("REST request to update user : {}", userDTO);
    userService.updateUser(userDTO);
}


}
