package com.library.libraryServer.services;

import com.library.libraryServer.domain.*;
import com.library.libraryServer.domain.Profile;
import com.library.libraryServer.domain.dto.*;
import com.library.libraryServer.domain.enums.*;
import com.library.libraryServer.mapper.*;
import com.library.libraryServer.repository.*;
import lombok.extern.log4j.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;

import javax.persistence.*;
import java.util.*;
import java.util.stream.*;

@Service
@Log4j2


public class ProfileService {
    private final ProfileRepository profileRepository;

    private final UserService userService;

    private final PaymentPlanService paymentPlanService;

    private final ProfileMapper profileMapper;

    public ProfileService(ProfileRepository profileRepository, @Lazy UserService userService, PaymentPlanService paymentPlanService, @Lazy ProfileMapper profileMapper) {
        this.profileRepository = profileRepository;
        this.userService = userService;
        this.paymentPlanService = paymentPlanService;
        this.profileMapper = profileMapper;
    }

    public List<ProfileDTO> getProfilesByUser(Long userId) {
        log.info("About to get profiles for user : {}", userId);

        Iterable<Profile> profiles = profileRepository.findAllByAccount(userId);

        List<ProfileDTO> profileDTOList = new ArrayList<>();

        for (Profile profile : profiles) {
            ProfileDTO profileDTO = profileMapper.toDTO(profile);
            profileDTOList.add(profileDTO);
        }

        return profileDTOList;
    }

    // save a profile
    public ProfileDTO save(Profile profile) throws EntityNotFoundException, EntityExistsException {

        log.debug("Request to save profile : {}", profile.getName());

        boolean nameExists = checkIfNameExistsForAccount(profile.getAccount(), profile.getName());

        if (nameExists) {
            throw new EntityExistsException("Profile with the supplied name exists for the user");
        }

        // check user account
        User user = userService.findById(profile.getAccount());

        if (user == null) {
            throw new EntityNotFoundException("User with the specified id does not exist");
        }

        PaymentPlanDTO paymentPlan = paymentPlanService.getOne(profile.getPlan());

        if (paymentPlan == null) {
            throw new EntityNotFoundException("Payment plan with the specified id does not exist");
        }

        // set account status
        profile.setAccountStatus(AccountStatus.UNPAID);

        profile = profileRepository.save(profile);

        ProfileDTO profileDTO = profileMapper.toDTO(profile);

        return profileDTO;
    }

    public boolean checkIfNameExistsForAccount(Long userId, String name) {

        List<Profile> profiles = findAllByAccount(userId);

        for (Profile profile : profiles) {
            if (profile.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }

    public List<Profile> findAllByAccount(Long userId) {
        log.info("About to get all profiles for logged in user");

        Iterable<Profile> profiles = profileRepository.findAllByAccount(userId);

        List<Profile> profileList = StreamSupport.stream(profiles.spliterator(), true).collect(Collectors.toList());

        return profileList;
    }

    public List<ProfileDTO> findAllByAccount() throws EntityNotFoundException {
        log.info("About to get all profiles for logged in user");

        User user = userService.getCurrentLoggedInUser();

        if (user == null) {
            throw new EntityNotFoundException("You have to be logged in to proceed");
        }

        Long userId = user.getId();

        Iterable<Profile> profiles = profileRepository.findAllByAccount(userId);

        List<ProfileDTO> profileDTOS = StreamSupport
                .stream(profiles.spliterator(), true)
                .map(profile -> profileMapper.toDTO(profile))
                .collect(Collectors.toList());

        return profileDTOS;
    }

    // get profile by accountId
    public Profile getOne(Long id) {

        log.debug("Request to get profile with id : {}", id);

        Optional<Profile> optionalProfile = profileRepository.findById(id);

        Profile profile = null;
        if (optionalProfile.isPresent()) {
            profile = optionalProfile.get();
        }

        return profile;
    }

    public Profile update(Profile profile) {
        log.info("Request to update profile : {} for user : {}", profile.getName(),
                profile.getAccount());

        profile = profileRepository.save(profile);

        return profile;
    }

    public ProfileDTO getOneDTO(Long id) {
        log.debug("About to map profile to dto");

        Profile profile = getOne(id);

        ProfileDTO profileDTO = profileMapper.toDTO(profile);

        return profileDTO;
    }
}
