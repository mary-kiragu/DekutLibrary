package com.library.libraryServer.domain.dto.daraja;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Data
public class DarajaSTKCallBack {

    @JsonProperty("MerchantRequestID")
    private String merchantRequestId;

    @JsonProperty("CheckoutRequestID")
    private String checkOutRequestId;

    @JsonProperty("ResultCode")
    private Integer resultCode;

    @JsonProperty("ResultDesc")
    private String resultDescription;

    @JsonProperty("CallbackMetadata")
    private CallBackMetaData callBackMetaData;

}
