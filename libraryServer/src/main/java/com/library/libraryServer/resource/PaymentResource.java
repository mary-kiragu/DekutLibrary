package com.library.libraryServer.resource;

import com.library.libraryServer.domain.dto.*;
import com.library.libraryServer.domain.dto.daraja.*;
import com.library.libraryServer.services.*;
import lombok.extern.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api/payment")
@Slf4j
public class PaymentResource {
    private final PayForSubscriptionService paymentService;

    public PaymentResource(PayForSubscriptionService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<DarajaRequestResponseDTO> initiatePayment(@RequestBody PaymentRequestDTO paymentRequestDTO){

       DarajaRequestResponseDTO darajaRequestResponseDTO= paymentService.initiatePayment(paymentRequestDTO);

        return ResponseEntity.ok().body(darajaRequestResponseDTO);

    }

    @PostMapping(path="/fines")
    public ResponseEntity<DarajaRequestResponseDTO> initiateFinePayment(@RequestBody FinePaymentRequestDTO finePaymentRequestDTO){

        DarajaRequestResponseDTO darajaRequestResponseDTO= paymentService.initiateFinePayment(finePaymentRequestDTO);

        return ResponseEntity.ok().body(darajaRequestResponseDTO);

    }
    @PostMapping("/callback")
    public void paymentCallBack(@RequestBody LipaNaDarajaCallBackDTO lipaNaDarajaCallBackDTO) {
        log.info("Lipa na daraja call back : {}", lipaNaDarajaCallBackDTO);

        paymentService.processPayment(lipaNaDarajaCallBackDTO);
    }


}
