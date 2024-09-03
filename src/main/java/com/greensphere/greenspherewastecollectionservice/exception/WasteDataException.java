package com.greensphere.greenspherewastecollectionservice.exception;

/**
 * Custom exception class for waste data validation errors.
 */
public class WasteDataException extends RuntimeException {

    public WasteDataException(String message) {
        super(message);
    }

    public WasteDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
