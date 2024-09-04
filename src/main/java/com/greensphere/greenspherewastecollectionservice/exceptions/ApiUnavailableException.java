package com.greensphere.greenspherewastecollectionservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class ApiUnavailableException extends RuntimeException {

    public ApiUnavailableException(String message) {
        super(message);
    }

}