package com.library.libraryServer.config;

import lombok.*;
import org.springframework.boot.context.properties.*;
import org.springframework.context.annotation.*;

@Data
@Configuration
@ConfigurationProperties(prefix = "mpesa.daraja")
public class MpesaConfiguration {
    private String consumerKey;
    private String consumerSecret;
    private String grantType;
    private String oauthEndpoint;

    private String shortCode;

    private String responseType;

    private String confirmationUrl;

    private String validationUrl;

    private String registerUrlEndpoint;

    private  String stkUrl;

    private Short paymentReminder;

    private String notifyUrl;

    private String notifyApikey;

    @Override
    public String toString() {
        return String.format("{consumerKey='%s', consumerSecret='%s', grantType='%s', oauthEndpoint='%s'}",
                consumerKey, consumerSecret, grantType, oauthEndpoint);
    }
}
