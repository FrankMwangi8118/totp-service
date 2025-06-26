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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class TotpServiceImpl implements TotpService {
    private static final String ISSUER = "coditApp";

    @Override
    public String generateTotpSecretKey() {
        GoogleAuthenticator authenticator = new GoogleAuthenticator();
        final GoogleAuthenticatorKey key = authenticator.createCredentials();
        return key.getKey();
    }

    @Override
    public String generateQrUrl(String username, String secretKey) {
         String url=String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s&algorithm=SHA1&digits=6&period=30",
                 URLEncoder.encode(ISSUER,StandardCharsets.UTF_8),
                 URLEncoder.encode(username, StandardCharsets.UTF_8),
                 secretKey,
                 URLEncoder.encode(ISSUER, StandardCharsets.UTF_8)
                 );
        try {
            return generateQrCode(url);
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }

    @Override
    public String generateQrCode(String qrContent) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1);

            BitMatrix bitMatrix = qrCodeWriter.encode(
                    qrContent, BarcodeFormat.QR_CODE, 400, 400, hints);
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            File output = new File("./qrcodes.png");
            ImageIO.write(qrImage, "png", output);
            log.info("QR code saved to: {}", output.getAbsolutePath());

            return output.getAbsolutePath();

        } catch (Exception e) {
            throw new RuntimeException("QR code generation failed", e);
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
