package com.library.libraryServer.domain.dto;

import lombok.*;

@Data
public class PaymentDTO {

    private String status;

    private String statusReason;

    private Long phoneNumber;

    private String transactionCode;

    private Long profileId;

    private double amount;

}
