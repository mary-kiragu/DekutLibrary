package com.library.libraryServer.services;

import com.library.libraryServer.domain.*;
import com.library.libraryServer.domain.dto.*;
import com.library.libraryServer.mapper.*;
import com.library.libraryServer.repository.*;
import lombok.extern.log4j.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
@Log4j2

public class PaymentPlanService {
    private final PaymentPlanRepository paymentPlanRepository;

    private final PaymentPlanMapper paymentPlanMapper;

    public PaymentPlanService(PaymentPlanRepository paymentPlanRepository, PaymentPlanMapper paymentPlanMapper) {
        this.paymentPlanRepository = paymentPlanRepository;
        this.paymentPlanMapper = paymentPlanMapper;
    }

    // save payment plan
    public PaymentPlanDTO save(PaymentPlanDTO paymentPlanDTO) {

        log.info("Request to save payment plan : {}", paymentPlanDTO);

        PaymentPlan paymentPlan = paymentPlanMapper.toEntity(paymentPlanDTO);

        paymentPlan = paymentPlanRepository.save(paymentPlan);

        return paymentPlanMapper.toDTO(paymentPlan);
    }
    // get all payment plans
    public List<PaymentPlanDTO> getAll() {
        log.info("Request to get all payment plans");

        List<PaymentPlan> paymentPlans = paymentPlanRepository.findAll();

        log.debug("PaymentPlans found: "+paymentPlans);
        return paymentPlanMapper.toDTO(paymentPlans);
    }

    // update payment plan
    public PaymentPlanDTO update(PaymentPlanDTO paymentPlanDTO) {

        if (paymentPlanDTO.getId() == null) {
            return save(paymentPlanDTO);
        }

        log.info("Request to update payment plan with id : {}", paymentPlanDTO.getId());

        PaymentPlan paymentPlan = paymentPlanMapper.toEntity(paymentPlanDTO);

        paymentPlan = paymentPlanRepository.save(paymentPlan);

        return paymentPlanMapper.toDTO(paymentPlan);
    }

    // delete payment plan
    public void deleteOne(Integer id) {

        log.info("About to delete payment plan with id : {}", id);

        paymentPlanRepository.deleteById(id);
    }

    // get specific plan
    public PaymentPlanDTO getOne(Integer id) {
        log.info("About to find payment plan with id : {}", id);

        Optional<PaymentPlan> optionalPaymentPlan = paymentPlanRepository.findById(id);

        PaymentPlan paymentPlan = null;

        if (optionalPaymentPlan.isPresent()) {
            paymentPlan = optionalPaymentPlan.get();
        } else {
            return null;
        }

        return paymentPlanMapper.toDTO(paymentPlan);
    }

    public PaymentPlan getOneEntity(Integer id) {
        log.info("About to find payment plan with id : {}", id);

        Optional<PaymentPlan> optionalPaymentPlan = paymentPlanRepository.findById(id);

        PaymentPlan paymentPlan = null;

        if (optionalPaymentPlan.isPresent()) {
            paymentPlan = optionalPaymentPlan.get();
        }

        return paymentPlan;
    }
}
