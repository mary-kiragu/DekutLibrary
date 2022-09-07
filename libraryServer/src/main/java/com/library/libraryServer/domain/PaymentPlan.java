package com.library.libraryServer.domain;

import com.library.libraryServer.domain.enums.*;
import lombok.*;

import javax.persistence.*;
@Data
@Entity
@Table(name = "tbl_payment_plans")
public class PaymentPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Integer paymentAmount;

    @Enumerated(EnumType.STRING)
    private PayDuration paymentDuration;

    private String description;
}
