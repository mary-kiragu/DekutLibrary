package com.library.libraryServer.domain;

import com.library.libraryServer.domain.enums.*;
import lombok.*;

import javax.persistence.*;
import java.time.*;

@Data
@Entity
@Table(name = "tbl_profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer plan;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    private Long account;

    private LocalDate lastBillingDate;

    private LocalDate nextBillingDate;
}
