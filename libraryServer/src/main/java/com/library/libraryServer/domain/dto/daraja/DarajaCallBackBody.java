package com.library.libraryServer.domain.dto.daraja;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Data
public class DarajaCallBackBody {

    @JsonProperty("stkCallback")
    private DarajaSTKCallBack stkCallBack;
}
