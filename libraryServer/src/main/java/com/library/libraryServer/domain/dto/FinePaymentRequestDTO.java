package com.library.libraryServer.domain.dto;

import lombok.*;

@Data
public class FinePaymentRequestDTO {
    private Long book;

    private Long phoneNumber;

    private Long userId;
}
