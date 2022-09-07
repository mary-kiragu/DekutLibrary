package com.library.libraryServer.mapper;

import com.library.libraryServer.domain.*;
import com.library.libraryServer.domain.Profile;
import com.library.libraryServer.domain.dto.*;
import com.library.libraryServer.services.*;
import org.springframework.stereotype.*;

@Service

public class ProfileMapper {
    private final PaymentPlanService paymentPlanService;
    private final UserService userService;

    public ProfileMapper(PaymentPlanService paymentPlanService, UserService userService) {
        this.paymentPlanService = paymentPlanService;
        this.userService = userService;
    }

    public ProfileDTO toDTO(Profile profile) {
        if (profile == null) {
            return null;
        }

        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(profile.getId());
        profileDTO.setName(profile.getName());

        profileDTO.setLastBillingDate(profile.getLastBillingDate());
        profileDTO.setNextBillingDate(profile.getNextBillingDate());

        PaymentPlanDTO paymentPlan = paymentPlanService.getOne(profile.getPlan());
        profileDTO.setPlan(paymentPlan);

        profileDTO.setAccountStatus(profile.getAccountStatus());

        User user = userService.findById(profile.getAccount());
        AccountDTO accountDTO = new AccountDTO(user.getId(), user.getName(), user.getEmail());
        profileDTO.setAccount(accountDTO);

        return profileDTO;
    }
}
