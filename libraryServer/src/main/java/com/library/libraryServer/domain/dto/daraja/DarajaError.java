package com.library.libraryServer.domain.dto.daraja;

import lombok.*;

@Data
public class DarajaError {

    private String requestId;

    private String errorCode;

    private String errorMessage;
}
