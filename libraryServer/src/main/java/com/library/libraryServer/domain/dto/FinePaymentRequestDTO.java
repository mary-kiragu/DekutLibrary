package com.library.libraryServer.domain.dto;

import lombok.*;

@Data
public class FinePaymentRequestDTO {
    private Long bookId;

    private Long phoneNumber;

    private Long userId;
}
