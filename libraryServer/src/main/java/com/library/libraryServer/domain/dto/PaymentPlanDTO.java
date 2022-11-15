package com.library.libraryServer.domain.dto;

import com.library.libraryServer.domain.enums.*;
import lombok.*;

import java.util.*;

@Data
public class PaymentPlanDTO {
    private Integer id;

    private String name;

    private Integer paymentAmount;

    private PayDuration paymentDuration;

    private String description;
}
