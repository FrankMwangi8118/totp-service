package com.codify.controller.dto;

import jakarta.annotation.Generated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author frank
 * email frank.mwangi@dadanadagroup.com
 * Created on 09/11/2025 19:10
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDto<T> {
    private Integer responseCode;
    private String responseMessage;
    private T data;
    private boolean isInternal;
}
