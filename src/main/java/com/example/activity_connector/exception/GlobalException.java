package com.example.activity_connector.exception;

import com.example.activity_connector.enums.ErrorCode;
import lombok.Getter;

public class GlobalException extends RuntimeException{

    @Getter
    private int code;

    @Getter
    private String message;

    public GlobalException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public GlobalException(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
}
