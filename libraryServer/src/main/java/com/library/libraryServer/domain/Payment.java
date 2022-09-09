package com.library.libraryServer.domain;

import com.library.libraryServer.domain.enums.*;
import lombok.*;

import javax.persistence.*;
import java.time.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tbl_payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String phoneNumber;

    private String transactionCode;

    private Long profileId;

    private String merchantRequestId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private Boolean isDamageFeePaid;

    private Integer amount;

    private LocalDateTime initiatedOn;

    public Payment(String email, String phoneNumber, String transactionCode, Long profileId, String merchantRequestId) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.transactionCode = transactionCode;
        this.profileId = profileId;
        this.merchantRequestId = merchantRequestId;
        this.status = PaymentStatus.PENDING;
    }
}
