package com.library.libraryServer.domain.dto;

import com.library.libraryServer.domain.enums.*;
import lombok.*;

import java.time.*;
@Data
public class ProfileDTO {
    private Long id;

    private String name;

   private PaymentPlanDTO plan;

    private AccountStatus accountStatus;

    private AccountDTO account;

    private LocalDate lastBillingDate;

    private LocalDate nextBillingDate;
}
