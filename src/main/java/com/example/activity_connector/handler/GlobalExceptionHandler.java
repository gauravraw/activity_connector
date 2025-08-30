package com.example.activity_connector.handler;

import com.example.activity_connector.exception.GlobalException;
import com.example.activity_connector.nonentity.BaseResponse;
import com.example.activity_connector.nonentity.Error;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<BaseResponse<String>> handleGlobalException(GlobalException ex) {
        BaseResponse<String> response = BaseResponse.<String>builder()
                .success(false)
                .data(Strings.EMPTY)
                .error(Error.builder()
                        .code(ex.getCode())
                        .message(ex.getMessage())
                        .build())
                .build();
        return ResponseEntity.ok(response);
    }

}
