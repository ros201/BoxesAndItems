package com.boxesanditems.exception;

public class MandatoryInputParamMissingException extends RuntimeException {
    public MandatoryInputParamMissingException(String param) {
        super("Mandatory parameter " + param + " is missing");
    }
}
