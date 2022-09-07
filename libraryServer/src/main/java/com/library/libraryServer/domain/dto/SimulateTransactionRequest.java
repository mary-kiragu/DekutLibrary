package com.library.libraryServer.domain.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Data
public class SimulateTransactionRequest {
    @JsonProperty("ShortCode")
    private String shortCode;

    @JsonProperty("Msisdn")
    private String msisdn;

    @JsonProperty("BillRefNumber")
    private String billRefNumber;

    @JsonProperty("Amount")
    private String amount;

    @JsonProperty("CommandID")
    private String commandID;
}
