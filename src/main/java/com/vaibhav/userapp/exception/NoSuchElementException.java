package com.vaibhav.userapp.exception;

public class NoSuchElementException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public NoSuchElementException() {
        super("resource not found on server");
    }
    public NoSuchElementException(String message) {
        super(message);
    }
}
