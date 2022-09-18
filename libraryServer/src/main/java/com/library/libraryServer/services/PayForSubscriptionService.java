package com.library.libraryServer.services;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.*;
import com.fasterxml.jackson.databind.*;
import com.library.libraryServer.config.*;
import com.library.libraryServer.domain.*;
import com.library.libraryServer.domain.dto.*;
import com.library.libraryServer.domain.dto.daraja.*;
import com.library.libraryServer.domain.enums.*;
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
public class PayForSubscriptionService {
    private final PaymentPlanService paymentPlanService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MpesaConfiguration mpesaConfiguration;

    private final PaymentRepository paymentRepository;

    private final ProfileService profileService;

    private final AuditLogService auditLogService;

    private final UserService userService;

    private final UserRepository userRepository;

    private LocalDateTime nextRefreshTime;

    private String accessToken;


    public PayForSubscriptionService(PaymentPlanService paymentPlanService, MpesaConfiguration mpesaConfiguration, PaymentRepository paymentRepository, ProfileService profileService, AuditLogService auditLogService, UserService userService, UserRepository userRepository) {
        this.paymentPlanService = paymentPlanService;
        this.mpesaConfiguration = mpesaConfiguration;
        this.paymentRepository = paymentRepository;
        this.profileService = profileService;
        this.auditLogService = auditLogService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public DarajaRequestResponseDTO initiatePayment(PaymentRequestDTO paymentRequestDTO) {

        DarajaRequestResponseDTO darajaRequestResponseDTO = new DarajaRequestResponseDTO();


        PaymentPlanDTO paymentPlanDTO = paymentPlanService.getOne(paymentRequestDTO.getPaymentPlan());
        if (paymentPlanDTO == null) {
            //throw exception
        }

        User user=userService.findById(paymentRequestDTO.getUserId());

        if(user==null){
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
        log.info("About to set payment plan for user");
        User loggedInUser = userService.getCurrentLoggedInUser();
        user.setPlan(paymentRequestDTO.getPaymentPlan());
        userService.update(user);


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


    public void processPayment(LipaNaDarajaCallBackDTO callBackDTO) {

        // find payment by merchant id
        Payment payment = findOneByMerchantRequestId(callBackDTO.getBody().getStkCallBack().getMerchantRequestId());
        // check if the payment involved paying damage fee
        User user = userService.getCurrentLoggedInUser();

        if (payment.getIsSubscriptionFeePaid()) {
            log.info("User paid subscription fee, updating the user....");

            userService.update(user);
        }

        PaymentDTO paymentDTO = new PaymentDTO();

        Map<String, Object> customFields = new HashMap<>();

        for (CallBackItem callBackItem : callBackDTO.getBody().getStkCallBack().getCallBackMetaData().getItem()) {
            customFields.put(callBackItem.getName(), callBackItem.getValue());
        }
        paymentDTO.setStatus("SUCCESS");
        paymentDTO.setStatusReason(callBackDTO.getBody().getStkCallBack().getResultDescription());
        paymentDTO.setPhoneNumber((Long) customFields.get("PhoneNumber"));
        paymentDTO.setTransactionCode((String) customFields.get("MpesaReceiptNumber"));
        paymentDTO.setAmount((Integer) customFields.get("Amount"));

        User profile = userService.findById(payment.getUserId());

        paymentDTO.setProfileId(payment.getUserId());

        // check if the payment was successfull
        Integer resultCode = callBackDTO.getBody().getStkCallBack().getResultCode();

        // this means that the transaction failed
        if (resultCode != 0) {

            // write audit logs here
            AuditLog auditLog = new AuditLog(
                    AuditAction.PAYMENT,
                    TransactionStatus.FAILURE,
                    callBackDTO.getBody().getStkCallBack().getResultDescription(),
                    payment.getEmail(),
                    payment.getPhoneNumber(),
                    payment.getUserId()
            );


            //auditLogService.save(auditLog);

            paymentDTO.setStatus("ERROR");
            paymentDTO.setStatusReason(callBackDTO.getBody().getStkCallBack().getResultDescription());
        }

        profile.setAccountStatus(AccountStatus.PAID);
        profile.setLastBillingDate(LocalDate.now());
        // get profile plan
        Integer planId = profile.getPlan();

        PaymentPlan paymentPlan = paymentPlanService.getOneEntity(planId);
        profile.setLastBillingDate(LocalDate.now());
        if (paymentPlan != null) {
            if (paymentPlan.getPaymentDuration().equals(PayDuration.MONTH)) {
                profile.setNextBillingDate(LocalDate.now().plusMonths(1));
            } else if (paymentPlan.getPaymentDuration().equals(PayDuration.YEAR)) {
                profile.setNextBillingDate(LocalDate.now().plusYears(1));
            } else if (paymentPlan.getPaymentDuration().equals(PayDuration.HOLIDAY)) {
                profile.setNextBillingDate(LocalDate.now().plusYears(1));
            }
        }

        userService.update(profile);

        payment.setTransactionCode(paymentDTO.getTransactionCode());
        payment.setStatus(PaymentStatus.COMPLETE);
        payment.setAmount(paymentDTO.getAmount());
        updatePayment(payment);

        // write audit logs
        AuditLog auditLog = new AuditLog(
                AuditAction.PAYMENT,
                TransactionStatus.SUCCESS,
                callBackDTO.getBody().getStkCallBack().getResultDescription(),
                payment.getEmail(),
                payment.getPhoneNumber(),
                profile.getId()
        );

       auditLogService.save(auditLog);


    }

    public Payment savePayment(Payment payment) {
        log.info("About to save successful payment  : {}", payment);

        Payment savedPayment = paymentRepository.save(payment);

        return savedPayment;
    }



    private Payment updatePayment(Payment payment) {
        log.info("Request to update payment by : {} on profile : {}", payment.getEmail(), payment.getUserId());

        Payment updatedPayment = paymentRepository.save(payment);

        return updatedPayment;
    }
    public Payment findOneByMerchantRequestId(String merchantRequestId) {

        log.info("Request to find payment with merchant request id : {}", merchantRequestId);

        Payment payment = null;

        Optional<Payment> optionalPayment = paymentRepository.findOneByMerchantRequestId(merchantRequestId);

        if (optionalPayment.isPresent()) {
            payment = optionalPayment.get();
        }

        return payment;
    }

    public Payment findByTransactionCode(String transactionCode) {

        Payment payment = null;
        log.info("About to find payment with transaction code : {}", transactionCode);

        Optional<Payment> optionalPayment = paymentRepository.findOneByTransactionCode(transactionCode);

        if (optionalPayment.isPresent()) {
            payment = optionalPayment.get();
        }

        return payment;
    }


}
