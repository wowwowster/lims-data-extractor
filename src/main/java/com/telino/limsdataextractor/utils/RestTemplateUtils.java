package com.telino.limsdataextractor.utils;

import org.springframework.http.HttpHeaders;

import javax.xml.bind.DatatypeConverter;

public class RestTemplateUtils {

    public static HttpHeaders addBasicAuth(HttpHeaders headers, String username, String password) {
        String plainCreds = username + ":" + password;
        byte[] plainCredsBytes = plainCreds.getBytes();
        String base64Creds = DatatypeConverter.printBase64Binary(plainCredsBytes);
        headers.add("Authorization", "Basic " + base64Creds);
        return headers;
    }

}
