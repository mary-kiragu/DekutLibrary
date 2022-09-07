package com.library.libraryServer.domain.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Data
public class SimulateTransactionResponse {
    @JsonProperty("ConversationID")
    private String conversationID;

    @JsonProperty("ResponseDescription")
    private String responseDescription;

    @JsonProperty("OriginatorCoversationID")
    private String originatorCoversationID;
}
