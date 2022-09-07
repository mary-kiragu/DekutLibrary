package com.library.libraryServer.util;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.springframework.security.crypto.codec.*;

import java.nio.charset.*;

public class HelperUtility {
    /**
     * @param value the value to be converted to a base64 string
     * @return returns base64String
     */
    public static String toBase64String(String value) {
        byte[] data = value.getBytes(StandardCharsets.ISO_8859_1);
        return String.valueOf(Base64.encode(data));
    }
    public static String toJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException exception) {
            return null;
        }
    }
}
