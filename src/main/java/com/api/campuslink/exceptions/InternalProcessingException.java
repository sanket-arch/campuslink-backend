package com.api.campuslink.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class InternalProcessingException extends RuntimeException {
    private final String responseMessage;
    private final HttpStatus responseStatus;

    public InternalProcessingException(String responseMessage, HttpStatus responseStatus) {
        super(responseMessage);
        this.responseMessage = responseMessage;
        this.responseStatus = responseStatus;
    }

}

