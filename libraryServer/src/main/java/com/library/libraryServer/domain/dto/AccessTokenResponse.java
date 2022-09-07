package com.library.libraryServer.domain.dto;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import lombok.*;
@Data
public class AccessTokenResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("requestId")
    private String requestId;

    @JsonProperty("expires_in")
    private String expiresIn;

    @SneakyThrows
    @Override
    public String toString() {
        return new ObjectMapper().writeValueAsString(this);
    }
}
