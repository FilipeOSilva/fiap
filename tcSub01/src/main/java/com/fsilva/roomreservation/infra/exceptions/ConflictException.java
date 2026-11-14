package com.fsilva.roomreservation.infra.exceptions;

public class ConflictException extends BusinessException {

    public ConflictException(String message) {
        super(message);
    }
}
