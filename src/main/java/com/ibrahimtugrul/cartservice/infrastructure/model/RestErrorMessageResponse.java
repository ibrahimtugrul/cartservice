package com.ibrahimtugrul.cartservice.infrastructure.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestErrorMessageResponse {
    private int code;
    private ExceptionType exceptionType;
    private String message;
    private String userMessage;
}