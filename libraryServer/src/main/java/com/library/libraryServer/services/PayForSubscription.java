package com.library.libraryServer.services;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.*;
import com.fasterxml.jackson.databind.*;
import com.library.libraryServer.config.*;
import com.library.libraryServer.domain.dto.*;
import com.library.libraryServer.domain.dto.daraja.*;
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
public class PayForSubscription {
    private final PaymentPlanService paymentPlanService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MpesaConfiguration mpesaConfiguration;

    private LocalDateTime nextRefreshTime;

    private String accessToken;

    public PayForSubscription(PaymentPlanService paymentPlanService, MpesaConfiguration mpesaConfiguration) {
        this.paymentPlanService = paymentPlanService;
        this.mpesaConfiguration = mpesaConfiguration;
    }

    public DarajaRequestResponseDTO initiatePayment(PaymentRequestDTO paymentRequestDTO) {

        DarajaRequestResponseDTO darajaRequestResponseDTO = new DarajaRequestResponseDTO();


        PaymentPlanDTO paymentPlanDTO = paymentPlanService.getOne(paymentRequestDTO.getPaymentPlan());
        if (paymentPlanDTO == null) {
            //throw exception
        }

        DarajaRequestDTO darajaRequestDTO = new DarajaRequestDTO();

        Integer paymentAmount = paymentPlanDTO.getPaymentAmount();

        darajaRequestDTO.setAmount(paymentAmount);

        darajaRequestDTO.setTimeStamp(makeTime());

        darajaRequestDTO.setPhoneNumber(paymentRequestDTO.getPhoneNumber());

        darajaRequestDTO.setPartyA(paymentRequestDTO.getPhoneNumber());
        //creating a password
        String passKey = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919";

        String toBeEncoded = darajaRequestDTO.getBusinessShortCode() + passKey + darajaRequestDTO.getTimeStamp();

        byte[] bytes = toBeEncoded.getBytes(StandardCharsets.UTF_8);
        bytes = Base64.getEncoder().encode(bytes);

        String encodedString = new String(bytes);
        darajaRequestDTO.setPassword(encodedString);

        String url = mpesaConfiguration.getStkUrl();
        //convert darajaRequestDto into a string to transport it over the network

        String body = null;
        try {
            body = objectMapper.writeValueAsString(darajaRequestDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + getAccessToken());

        String response = null;
        try {
            response = HttpUtil.post(url, body, headerMap, MediaType.get("application/json; charset=utf-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        DarajaResponseDTO darajaACKDTO = null;
        DarajaError darajaError = null;
        try {
            darajaACKDTO = objectMapper.readValue(response, new TypeReference<DarajaResponseDTO>() {
            });
        } catch (JsonProcessingException e) {
            try {
                darajaError = objectMapper.readValue(response, new TypeReference<DarajaError>() {
                });
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
            }
        }

        log.info("Acknowledgement from daraja : {}", darajaACKDTO);

        if (darajaError != null) {
            darajaRequestResponseDTO.setError(darajaError);
        }

        if (darajaACKDTO != null) {
            darajaRequestResponseDTO.setResponse(darajaACKDTO);
        }


        return darajaRequestResponseDTO;
    }

    public DarajaAuthResponseDTO getAuth() {

        String url = mpesaConfiguration.getOauthEndpoint();
        String consumerKey = mpesaConfiguration.getConsumerKey();
        String consumerSecret = mpesaConfiguration.getConsumerSecret();

        log.info("consumer key : {} consumer secret : {} url : {}", consumerKey, consumerSecret, url);


        String toBeEncoded = consumerKey + ":" + consumerSecret;
        byte[] bytes = toBeEncoded.getBytes(StandardCharsets.UTF_8);
        bytes = Base64.getEncoder().encode(bytes);
        String encodedString = new String(bytes);

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Basic " + encodedString);

        String response = null;
        try {
            response = HttpUtil.get(url, headerMap, MediaType.get("application/json; charset=utf-8"));
        } catch (IOException e) {

        }
        log.info("Response {}", response);

        DarajaAuthResponseDTO darajaAuthResponseDTO = null;

        try {
            darajaAuthResponseDTO = objectMapper.readValue(response, new TypeReference<DarajaAuthResponseDTO>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        log.info("Found access token : {}", darajaAuthResponseDTO.getAccessToken());

        this.nextRefreshTime = LocalDateTime.now().plusMinutes(59);

        this.accessToken = darajaAuthResponseDTO.getAccessToken();

        return darajaAuthResponseDTO;
    }

    private String getAccessToken() {

        DarajaAuthResponseDTO darajaAuthResponseDTO = null;

        if (this.nextRefreshTime == null) {
            darajaAuthResponseDTO = getAuth();
            return darajaAuthResponseDTO.getAccessToken();
        }

        if (LocalDateTime.now().isBefore(this.nextRefreshTime)) {
            log.info("There exists an access token, no need to call auth");
            return this.accessToken;
        } else {
            log.info("I have to call auth for now....");
            darajaAuthResponseDTO = getAuth();
        }

        return darajaAuthResponseDTO.getAccessToken();
    }

    public String makeTime() {
        int year = LocalDate.now().getYear();

        int month = LocalDate.now().getMonthValue();
        String monthString = month + "";
        if (month < 10) {
            monthString = "0" + month;
        }

        int day = LocalDate.now().getDayOfMonth();
        String dayString = day + "";
        if (day < 10) {
            dayString = "0" + day;
        }

        int hour = LocalDateTime.now().getHour();
        String hourString = hour + "";
        if (hour < 10) {
            hourString = "0" + hour;
        }

        int minute = LocalDateTime.now().getMinute();
        String minuteString = minute + "";
        if (minute < 10) {
            minuteString = "0" + minute;
        }

        int seconds = LocalDateTime.now().getSecond();
        String secondString = seconds + "";
        if (seconds < 10) {
            secondString = "0" + seconds;
        }


        return year + monthString + dayString + hourString + minuteString + secondString;
    }

}
