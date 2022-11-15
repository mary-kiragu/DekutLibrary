package com.library.libraryServer.mapper;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.*;
import com.fasterxml.jackson.databind.*;
import com.library.libraryServer.domain.*;
import com.library.libraryServer.domain.dto.*;
import lombok.extern.slf4j.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
@Slf4j
public class PaymentPlanMapper {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PaymentPlanDTO toDTO(PaymentPlan paymentPlan) {

        if (paymentPlan == null) {
            return null;
        }

        PaymentPlanDTO paymentPlanDTO = new PaymentPlanDTO();
        paymentPlanDTO.setId(paymentPlan.getId());
        paymentPlanDTO.setName(paymentPlan.getName());
        paymentPlanDTO.setPaymentAmount(paymentPlan.getPaymentAmount());
        paymentPlanDTO.setPaymentDuration(paymentPlan.getPaymentDuration());
        paymentPlanDTO.setDescription(paymentPlan.getDescription());

//        try {
//            paymentPlanDTO.setDescription(objectMapper.readValue(paymentPlan.getDescription(), new TypeReference<List<String>>() {
//            }));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }

        return paymentPlanDTO;
    }

    public PaymentPlan toEntity(PaymentPlanDTO paymentPlanDTO) {
        if (paymentPlanDTO == null) {
            return null;
        }

        PaymentPlan paymentPlan = new PaymentPlan();
        paymentPlan.setId(paymentPlanDTO.getId());
        paymentPlan.setName(paymentPlanDTO.getName());
        paymentPlan.setPaymentAmount(paymentPlanDTO.getPaymentAmount());
        paymentPlan.setPaymentDuration(paymentPlanDTO.getPaymentDuration());
        paymentPlan.setDescription(paymentPlanDTO.getDescription());

//        try {
//            paymentPlan.setDescription(objectMapper.writeValueAsString(paymentPlanDTO.getDescription()));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }

        return paymentPlan;
    }

    public List<PaymentPlanDTO> toDTO(List<PaymentPlan> paymentPlans) {

        List<PaymentPlanDTO> paymentPlanDTOS = new ArrayList<>();
        log.debug("to dto method");

        for (PaymentPlan paymentPlan : paymentPlans) {
            PaymentPlanDTO paymentPlanDTO = toDTO(paymentPlan);
            paymentPlanDTOS.add(paymentPlanDTO);
        }

        return paymentPlanDTOS;
    }

    public List<PaymentPlan> toEntity(List<PaymentPlanDTO> paymentPlanDTOS) {

        List<PaymentPlan> paymentPlans = new ArrayList<>();

        for (PaymentPlanDTO paymentPlanDTO : paymentPlanDTOS) {
            PaymentPlan paymentPlan = toEntity(paymentPlanDTO);
            paymentPlans.add(paymentPlan);
        }

        return paymentPlans;
    }
}
