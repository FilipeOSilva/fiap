package com.fsilva.roomreservation.infra.exceptions;

public class ForbiddenException extends BusinessException {

    public ForbiddenException(String message) {
        super(message);
    }
}
