package com.library.libraryServer.services;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.*;
import com.fasterxml.jackson.databind.*;
import com.library.libraryServer.config.*;
import com.library.libraryServer.domain.*;
import com.library.libraryServer.domain.dto.*;
import com.library.libraryServer.domain.dto.daraja.*;
import com.library.libraryServer.repository.*;
import com.library.libraryServer.util.*;
import lombok.extern.slf4j.*;
import okhttp3.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.nio.charset.*;
import java.time.*;
import java.util.*;

@Service
@Slf4j
public class FinesPaymentService {

    private final BookService bookService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MpesaConfiguration mpesaConfiguration;

    private final PaymentRepository paymentRepository;

    private final UserService userService;



    private LocalDateTime nextRefreshTime;

    private String accessToken;


    public FinesPaymentService(BookService bookService, MpesaConfiguration mpesaConfiguration, PaymentRepository paymentRepository, UserService userService) {
        this.bookService = bookService;
        this.mpesaConfiguration = mpesaConfiguration;
        this.paymentRepository = paymentRepository;
        this.userService = userService;
    }

//    public DarajaRequestResponseDTO initiateFinePayment(FinePaymentRequestDTO finePaymentRequestDTO) {
//
//        DarajaRequestResponseDTO darajaRequestResponseDTO = new DarajaRequestResponseDTO();
//
//
//        Optional<Book> bookOptional = bookService.getOneBook(finePaymentRequestDTO.getBook());
//        if (bookOptional.isPresent()) {
//            Book book=bookOptional.get();
//            User user=userService.findById(finePaymentRequestDTO.getUserId());
//
//            if(user==null){
//                //throw exception
//            }
//            DarajaRequestDTO darajaRequestDTO = new DarajaRequestDTO();
//
//            Integer paymentAmount = book.getFine();
//
//            darajaRequestDTO.setAmount(paymentAmount);
//
//            darajaRequestDTO.setTimeStamp(makeTime());
//
//            darajaRequestDTO.setPhoneNumber(finePaymentRequestDTO.getPhoneNumber());
//
//            darajaRequestDTO.setPartyA(finePaymentRequestDTO.getPhoneNumber());
//            //creating a password
//            String passKey = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919";
//
//            String toBeEncoded = darajaRequestDTO.getBusinessShortCode() + passKey + darajaRequestDTO.getTimeStamp();
//
//            byte[] bytes = toBeEncoded.getBytes(StandardCharsets.UTF_8);
//            bytes = Base64.getEncoder().encode(bytes);
//
//            String encodedString = new String(bytes);
//            darajaRequestDTO.setPassword(encodedString);
//
//            String url = mpesaConfiguration.getStkUrl();
//            //convert darajaRequestDto into a string to transport it over the network
//
//            String body = null;
//            try {
//                body = objectMapper.writeValueAsString(darajaRequestDTO);
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//            Map<String, String> headerMap = new HashMap<>();
//            headerMap.put("Authorization", "Bearer " + getAccessToken());
//
//            String response = null;
//            try {
//                response = HttpUtil.post(url, body, headerMap, MediaType.get("application/json; charset=utf-8"));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            DarajaResponseDTO darajaACKDTO = null;
//            DarajaError darajaError = null;
//            try {
//                darajaACKDTO = objectMapper.readValue(response, new TypeReference<DarajaResponseDTO>() {
//                });
//            } catch (JsonProcessingException e) {
//                try {
//                    darajaError = objectMapper.readValue(response, new TypeReference<DarajaError>() {
//                    });
//                } catch (JsonProcessingException ex) {
//                    ex.printStackTrace();
//                }
//            }
//
//            log.info("Acknowledgement from daraja : {}", darajaACKDTO);
//
//            if (darajaError != null) {
//                darajaRequestResponseDTO.setError(darajaError);
//            }
//
//            if (darajaACKDTO != null) {
//                darajaRequestResponseDTO.setResponse(darajaACKDTO);
//            }
//            log.info("About to set payment plan for user");
//            User loggedInUser = userService.getCurrentLoggedInUser();
//            user.setPlan(Math.toIntExact(finePaymentRequestDTO.getBook()));
//            userService.update(user);
//            // save payment
//            Payment payment = new Payment(
//                    user.getEmail(),
//                    finePaymentRequestDTO.getPhoneNumber() + "",
//                    null,
//                    finePaymentRequestDTO.getUserId(),
//                    darajaACKDTO.getMerchantRequestId()
//            );
//            payment.setInitiatedOn(LocalDateTime.now());
//
//
//            log.info("About to save payment with merchant id : {}", darajaACKDTO.getMerchantRequestId());
//            savePayment(payment);
//
//
//
//        }
//        return darajaRequestResponseDTO;
//
//
//    }

}
