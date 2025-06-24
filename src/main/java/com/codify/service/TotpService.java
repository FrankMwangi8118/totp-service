package com.codify.service;

public interface TotpService {
    //generate secret
    //generate qrUrl
    //create qrCode
    //validated code (boolean)

    String generateTotpSecretKey();

    String generateQrUrl(String username, String secretKey);

    String generateQrCode(String qrUrl);

    boolean isValid(String secretKey,int code);
}
