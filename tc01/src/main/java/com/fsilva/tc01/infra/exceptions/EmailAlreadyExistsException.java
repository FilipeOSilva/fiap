package com.fsilva.tc01.infra.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EmailAlreadyExistsException extends BusinessException {

    public EmailAlreadyExistsException() {
        super("E-mail já cadastrado");
    }
}
