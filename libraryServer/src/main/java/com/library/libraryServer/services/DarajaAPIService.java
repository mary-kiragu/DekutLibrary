package com.library.libraryServer.services;

import com.fasterxml.jackson.databind.*;
import com.library.libraryServer.config.*;
import com.library.libraryServer.domain.dto.*;
import com.library.libraryServer.util.*;
import lombok.extern.slf4j.*;
import okhttp3.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.util.*;

import static com.library.libraryServer.util.Constants.*;

@Service
@Slf4j
public class DarajaAPIService {
    private final MpesaConfiguration mpesaConfiguration;
    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;

    public DarajaAPIService(MpesaConfiguration mpesaConfiguration, OkHttpClient okHttpClient, ObjectMapper objectMapper) {
        this.mpesaConfiguration = mpesaConfiguration;
        this.okHttpClient = okHttpClient;
        this.objectMapper = objectMapper;
    }

    /**
     * @return Returns Daraja API Access Token Response
     */
        public AccessTokenResponse getAccessToken() {

        // get the Base64 rep of consumerKey + ":" + consumerSecret
        String encodedCredentials = HelperUtility.toBase64String(String.format("%s:%s", mpesaConfiguration.getConsumerKey(),
                mpesaConfiguration.getConsumerSecret()));

        Request request = new Request.Builder()
                .url(String.format("%s?grant_type=%s", mpesaConfiguration.getOauthEndpoint(), mpesaConfiguration.getGrantType()))
                .get()
                .addHeader(AUTHORIZATION_HEADER_STRING, String.format("%s %s", BASIC_AUTH_STRING, encodedCredentials))
                .addHeader(CACHE_CONTROL_HEADER, CACHE_CONTROL_HEADER_VALUE)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            assert response.body() != null;

            // use Jackson to Decode the ResponseBody ...
            return objectMapper.readValue(response.body().string(), AccessTokenResponse.class);
        } catch (IOException e) {
            log.error(String.format("Could not get access token. -> %s", e.getLocalizedMessage()));
            return null;
        }
    }

    //Register url request
    public RegisterUrlResponse registerUrl() {
        AccessTokenResponse accessTokenResponse = getAccessToken();

        RegisterUrlRequest registerUrlRequest = new RegisterUrlRequest();
        registerUrlRequest.setConfirmationURL(mpesaConfiguration.getConfirmationUrl());
        registerUrlRequest.setResponseType(mpesaConfiguration.getResponseType());
        registerUrlRequest.setShortCode(mpesaConfiguration.getShortCode());
        registerUrlRequest.setValidationURL(mpesaConfiguration.getValidationUrl());


        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE,
                Objects.requireNonNull(HelperUtility.toJson(registerUrlRequest)));

        Request request = new Request.Builder()
                .url(mpesaConfiguration.getRegisterUrlEndpoint())
                .post(body)
                .addHeader("Authorization", String.format("%s %s", BEARER_AUTH_STRING, accessTokenResponse.getAccessToken()))
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();

            assert response.body() != null;
            // use Jackson to Decode the ResponseBody ...
            return objectMapper.readValue(response.body().string(), RegisterUrlResponse.class);

        } catch (IOException e) {
            log.error(String.format("Could not register url -> %s", e.getLocalizedMessage()));
            return null;
        }
    }
}
