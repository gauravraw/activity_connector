package com.example.activity_connector.enums;

import lombok.Getter;
import lombok.Setter;

public enum ErrorCode {

    INTERNAL_SERVER_ERROR(500, "Internal Server Error"), TOO_MANY_REQUESTS(429, "Too Many Requests");

    @Getter
    @Setter
    private int code;

    @Getter
    @Setter
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
