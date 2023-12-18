package com.example.demo.exception;

public class InvoiceDetailsNotFound extends RuntimeException{
    public InvoiceDetailsNotFound(String mensaje) {
        super(mensaje);
    }
}
