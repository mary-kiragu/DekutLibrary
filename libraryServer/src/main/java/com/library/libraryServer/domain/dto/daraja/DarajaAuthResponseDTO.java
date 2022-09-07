package com.library.libraryServer.domain.dto.daraja;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Data
public class DarajaAuthResponseDTO {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private String expiresIn;
}
