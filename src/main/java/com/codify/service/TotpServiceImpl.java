package com.codify.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.Authenticator;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class TotpServiceImpl implements TotpService {
    private static final String ISSUER = "mwas3";

    @Override
    public String generateTotpSecretKey() {
        GoogleAuthenticator authenticator = new GoogleAuthenticator();
        final GoogleAuthenticatorKey key = authenticator.createCredentials();
        return key.getKey();
    }

    @Override
    public String generateQrUrl(String username, String secretKey) {
        String url = GoogleAuthenticatorQRGenerator.getOtpAuthURL(
                ISSUER,
                username,
                new GoogleAuthenticatorKey.Builder(secretKey).build());
        log.info("qr_url{}", url);
        try {
            return generateQrCode(url);
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }

    @Override
    public String generateQrCode(String qrUrl) {

        try {

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = qrCodeWriter.encode(qrUrl, BarcodeFormat.QR_CODE, 300, 300, hints);
            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            File dir = new File("./", "qr");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File outPut = new File(dir + "qr.png");
            ImageIO.write(image, "png", outPut);
            log.info("qr_path:{}", outPut.getAbsolutePath());
            return outPut.getAbsolutePath();

        } catch (Exception e) {
            log.error("Failed to generate QR code", e);
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }

    @Override
    public boolean isValid(String secretKey, int code) {
        GoogleAuthenticator authenticator = new GoogleAuthenticator(
                new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder()
                        .setWindowSize(1)
                        .build()
        );
        return authenticator.authorize(secretKey, code);
    }
}
