package com.example.demo.exception;

public class InvoiceNotFoundException extends RuntimeException{

    public  InvoiceNotFoundException(String mensaje) {
        super(mensaje);
    }
}
