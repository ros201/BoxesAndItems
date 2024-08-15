package com.boxesanditems.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(MandatoryInputParamMissingException.class)
    public ResponseEntity<String> handleMandatoryInputParamMissingException(MandatoryInputParamMissingException e) {
        log.error("Error occurred:" + e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
