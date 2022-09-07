package com.library.libraryServer.domain.dto.daraja;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Data
public class DarajaResponseDTO {

    @JsonProperty("MerchantRequestID")
    private String merchantRequestId;

    @JsonProperty("CheckoutRequestID")
    private String checkOutRequestId;

    @JsonProperty("ResponseCode")
    private String responseCode;

    @JsonProperty("ResponseDescription")
    private String responseDescription;

    @JsonProperty("CustomerMessage")
    private String customerMessage;
}
