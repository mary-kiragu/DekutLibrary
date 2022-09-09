package com.library.libraryServer.domain.dto.daraja;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Data
public class LipaNaDarajaCallBackDTO {

    @JsonProperty("Body")
    private DarajaCallBackBody body;

}
