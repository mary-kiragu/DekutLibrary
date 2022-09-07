package com.library.libraryServer.domain.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Data
public class RegisterUrlRequest {
    @JsonProperty("ShortCode")
    private String shortCode;

    @JsonProperty("ConfirmationURL")
    private String confirmationURL;

    @JsonProperty("ValidationURL")
    private String validationURL;

    @JsonProperty("ResponseType")
    private String responseType;
}
