package com.softuni.fitlaunch.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DublicateObjectException extends MyCustomException{

    public DublicateObjectException(String field, String message) {
        super(field, message);
    }
}
