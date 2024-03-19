package com.softuni.fitlaunch.service.exception;

public class MyCustomException extends RuntimeException{

    private String field;
    private String message;

    public MyCustomException(String message) {
        this.message = message;
    }

    public MyCustomException(String field, String message) {
        this.field = field;
        this.message = message;
    }
}
