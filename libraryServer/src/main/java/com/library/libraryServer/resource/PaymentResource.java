package com.library.libraryServer.resource;

import com.library.libraryServer.domain.dto.*;
import com.library.libraryServer.domain.dto.daraja.*;
import com.library.libraryServer.services.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api/payment")
public class PaymentResource {
    private final PayForSubscription paymentService;

    public PaymentResource(PayForSubscription paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<DarajaRequestResponseDTO> initiatePayment(@RequestBody PaymentRequestDTO paymentRequestDTO){

       DarajaRequestResponseDTO darajaRequestResponseDTO= paymentService.initiatePayment(paymentRequestDTO);

        return ResponseEntity.ok().body(darajaRequestResponseDTO);

    }


}
