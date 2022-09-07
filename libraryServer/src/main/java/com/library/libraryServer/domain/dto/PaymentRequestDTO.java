package com.library.libraryServer.domain.dto;

import lombok.*;

@Data
public class PaymentRequestDTO {

    private Integer paymentPlan;

    private Long phoneNumber;
}
