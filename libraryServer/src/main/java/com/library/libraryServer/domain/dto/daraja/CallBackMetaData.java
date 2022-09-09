package com.library.libraryServer.domain.dto.daraja;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.util.*;

@Data
public class CallBackMetaData {

    @JsonProperty("Item")
    private List<CallBackItem> item;
}
