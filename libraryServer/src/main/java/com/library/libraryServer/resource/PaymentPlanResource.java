package com.library.libraryServer.resource;

import com.library.libraryServer.domain.dto.*;
import com.library.libraryServer.services.*;
import lombok.extern.slf4j.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Slf4j
@RequestMapping(path= "/api")
public class PaymentPlanResource {
    private final PaymentPlanService paymentPlanService;

    public PaymentPlanResource(PaymentPlanService paymentPlanService) {
        this.paymentPlanService = paymentPlanService;
    }
    @PostMapping(path="/payment-plan")
    public PaymentPlanDTO save(@RequestBody PaymentPlanDTO paymentPlanDTO) {

        return paymentPlanService.save(paymentPlanDTO);
    }

    @GetMapping(path="/payment-plan")
    public List<PaymentPlanDTO> getAll() {
     log.debug("Rest request to find all payment plans: ");
        return paymentPlanService.getAll();
    }

    @PutMapping(path="/payment-plan")
    public PaymentPlanDTO update(@RequestBody PaymentPlanDTO paymentPlanDTO) {
        log.debug("Request to update plan");

        return paymentPlanService.update(paymentPlanDTO);
    }

    @GetMapping("/payment-plan/{id}")
    public PaymentPlanDTO getOne(@PathVariable Integer id) {

        return paymentPlanService.getOne(id);
    }

    @DeleteMapping("/payment-plan/{id}")
    public void deleteOne(@PathVariable Integer id) {
        paymentPlanService.deleteOne(id);
    }
}
