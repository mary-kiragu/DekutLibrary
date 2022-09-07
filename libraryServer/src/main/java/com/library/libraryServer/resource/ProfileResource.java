package com.library.libraryServer.resource;

import com.library.libraryServer.domain.*;
import com.library.libraryServer.domain.dto.*;
import com.library.libraryServer.resource.vms.*;
import com.library.libraryServer.services.*;
import lombok.extern.log4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.persistence.*;
import java.util.*;
@Log4j2
@RestController
@RequestMapping("/api")
public class ProfileResource {


        private final ProfileService profileService;

        public ProfileResource(ProfileService profileService) {
            this.profileService = profileService;
        }

        @PostMapping("/profiles")
        public ResponseEntity save(@RequestBody Profile profile){

            ProfileDTO profileDTO = null;
            try {
                profileDTO = profileService.save(profile);
            } catch (EntityNotFoundException e) {
                ErrorVM errorVM = new ErrorVM(
                        "The plan or account specified was not found"
                );

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorVM);
            } catch (EntityExistsException e) {
                ErrorVM errorVM = new ErrorVM(
                        "A profile exists with the specified name for this user"
                );

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorVM);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(profileDTO);
        }

        @GetMapping("/profiles/by-user")
        public ResponseEntity getAllForLoggedInUser(){

            List<ProfileDTO> profileDTOS = new ArrayList<>();
            try {
                profileDTOS = profileService.findAllByAccount();
            } catch (EntityNotFoundException e) {
                ErrorVM errorVM = new ErrorVM(
                        "User is not logged in"
                );

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorVM);
            }

            return ResponseEntity.ok().body(profileDTOS);
        }


    }


