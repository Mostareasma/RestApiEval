package com.example.server.exceptions;

public class BookNotFoundException extends Exception {

    public BookNotFoundException(String message) {
        super(message);
    }
}