package com.codify.controller;

import com.codify.dto.LoginDto;
import com.codify.dto.RegistrationDto;
import com.codify.service.TotpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class TotpController {
    private final TotpService totpService;

    public TotpController(TotpService totpService) {
        this.totpService = totpService;
    }

    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<?> register(@RequestBody RegistrationDto registrationDto) {

        var secret = totpService.generateTotpSecretKey();
        var qrUrl = totpService.generateQrUrl(registrationDto.username(), secret);
log.debug("url{}",qrUrl);
        //create the response
        Map<String, String> response = new HashMap<>();
        response.put("qrUrl", qrUrl);
        response.put("secret", secret);

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/auth", consumes = "application/json")
    public ResponseEntity<?> validateTotp(@RequestBody LoginDto loginDto) {

        boolean isValid = totpService.isValid( loginDto.secretKey(),loginDto.totpCode());
        if (!isValid) {
            log.error("Invalid Totp Code");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok("success");

    }


}
