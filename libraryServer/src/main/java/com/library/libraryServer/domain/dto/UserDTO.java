package com.library.libraryServer.domain.dto;

import com.library.libraryServer.domain.enums.*;
import lombok.*;

import javax.persistence.*;
import java.time.*;

@Data
public class UserDTO {
    private Long id;

    private String email;

    private String name;

    private String password;

    private String phoneNumber;

    private Authority authority;

    private Integer plan;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    private Long account;

    private LocalDate lastBillingDate;

    private LocalDate nextBillingDate;
}
