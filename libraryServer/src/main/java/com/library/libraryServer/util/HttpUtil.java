package com.library.libraryServer.util;

import lombok.extern.log4j.*;
import okhttp3.*;

import java.io.*;
import java.util.*;

@Log4j2
public class HttpUtil {
    public static String post(String url, String body, Map<String, String> headerMap, MediaType mediaType) throws IOException {

        String name = null;

        String value = null;

        for (Map.Entry<String, String> pair : headerMap.entrySet()) {
            name = pair.getKey();
            value = pair.getValue();
        }

        log.debug("Util has received the request");
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(body, mediaType);
        Request request = new Request.Builder()
                .url(url)
                .addHeader(name, value)
                .post(requestBody)
                .build();


        log.debug("Headers {}", request.headers());

        log.debug("Request {}", request);
        try (
                Response response = client.newCall(request).execute();
        ) {
            return response.body().string();

        }

    }

    public static String post(String url, String body, MediaType mediaType) throws IOException {
        log.debug("Util has received the request");
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(body, mediaType);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        try (
                Response response = client.newCall(request).execute();
        ) {
            return response.body().string();
        }

    }

    public static String get(String url, Map<String, String> headerMap, MediaType mediaType) throws IOException {

        String name = null;

        String value = null;

        for (Map.Entry<String, String> pair : headerMap.entrySet()) {
            name = pair.getKey();
            value = pair.getValue();
        }

        log.debug("Util has received the request");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader(name, value)
                .get()
                .build();


        log.debug("Headers {}", request.headers());

        log.debug("Request {}", request);
        try (
                Response response = client.newCall(request).execute();
        ) {
            return response.body().string();

        }

    }

}
