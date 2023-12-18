package com.example.demo.exception;

public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException(String mensaje) {
        super(mensaje);
    }
}
