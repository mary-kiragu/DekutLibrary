package com.library.libraryServer.domain.dto.daraja;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Data
public class DarajaRequestDTO {

    @JsonProperty("BusinessShortCode")
    private Long businessShortCode;

    @JsonProperty("Password")
    private String password;

    @JsonProperty("Timestamp")
    private String timeStamp;

    @JsonProperty("TransactionType")
    private String transactionType;

    @JsonProperty("Amount")
    private Integer amount;

    @JsonProperty("PartyA")
    private Long partyA;

    @JsonProperty("PartyB")
    private Long partyB;

    @JsonProperty("PhoneNumber")
    private Long phoneNumber;

    @JsonProperty("CallBackURL")
    private String callBackUrl;

    @JsonProperty("AccountReference")
    private String accountReference;

    @JsonProperty("TransactionDesc")
    private String transactionDesc;

    public DarajaRequestDTO() {
        this.businessShortCode = 174379L;
        this.transactionType = "CustomerPayBillOnline";
        this.partyB = 174379L;
        this.callBackUrl = "https://posthere.io/7edb-4ed5-9fe6";
        this.accountReference = "DEKUT Library";
        this.transactionDesc = "Account Payment/Renewal";
    }

}
