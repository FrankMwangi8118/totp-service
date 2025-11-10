package com.codify.controller;

import com.codify.controller.dto.LoginDto;
import com.codify.controller.dto.RegistrationDto;
import com.codify.controller.dto.RegistrationResponseDto;
import com.codify.controller.dto.ResponseDto;
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
    public ResponseEntity<ResponseDto<RegistrationResponseDto>> register(@RequestBody RegistrationDto registrationDto) {

        var secret = totpService.generateTotpSecretKey();
        var qrUrl = totpService.generateQrUrl(
                registrationDto.username(),
                secret);



        try {
            return ResponseEntity.ok().body(
                    ResponseDto.<RegistrationResponseDto>builder()
                            .responseCode(200)
                            .responseMessage("created successfully")
                            .data(new RegistrationResponseDto(qrUrl, secret))
                            .isInternal(false)
                            .build()
            );
        }catch (Exception e){
            return ResponseEntity.badRequest().body(
                    ResponseDto.<RegistrationResponseDto>builder()
                            .responseCode(400)
                            .responseMessage("Bad Request")
                            .isInternal(false)
                            .build()
            );
        }

    }
    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<?> validateTotp(@RequestBody LoginDto loginDto) {

        boolean isValid = totpService.isValid(loginDto.secretKey(), loginDto.totpCode());
        if (!isValid) {
            log.error("Invalid Totp Code");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok("success");

    }


}
