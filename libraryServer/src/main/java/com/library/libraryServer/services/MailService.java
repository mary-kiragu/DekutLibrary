package com.library.libraryServer.services;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.library.libraryServer.config.*;
import com.library.libraryServer.domain.dto.*;
import com.library.libraryServer.util.*;
import okhttp3.*;
import org.springframework.stereotype.*;

import javax.management.*;
import java.io.*;
import java.util.*;

@Service
public class MailService {

    private final MpesaConfiguration mpesaConfiguration;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public MailService( MpesaConfiguration mpesaConfiguration) {
        this.mpesaConfiguration = mpesaConfiguration;}


    public Notification sendEmail(NotifyEmailDTO notifyEmailDTO) {
        String url = mpesaConfiguration.getNotifyUrl() + "send-email";

        String body = null;
        try {
            body = objectMapper.writeValueAsString(notifyEmailDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("apikey", mpesaConfiguration.getNotifyUrl());

        try {
            HttpUtil.post(url, body, headerMap, MediaType.get("application/json; charset=utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
