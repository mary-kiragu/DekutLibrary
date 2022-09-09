package com.library.libraryServer.domain.dto.daraja;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Data
public class CallBackItem {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Value")
    private Object value;
}
