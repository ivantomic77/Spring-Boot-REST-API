package com.example.accessdatawjpa.exception;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(Long id) {
        super("Could not find employee " + id);
    }
    public CustomerNotFoundException(boolean Active) {
        super("Could not find employee with status " + Active);
    }
}