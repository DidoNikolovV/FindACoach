package com.softuni.fitlaunch.service.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class ObjectNotFoundException extends MyCustomException {
    public ObjectNotFoundException(String message) {
        super(message);
    }
}
