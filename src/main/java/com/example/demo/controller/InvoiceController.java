package com.example.demo.controller;


import com.example.demo.exception.InvoiceNotFoundException;
import com.example.demo.model.Invoice;
import com.example.demo.model.InvoiceDTO;
import com.example.demo.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(path = "api/invoice")
@RestController
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/")
    public ResponseEntity<InvoiceDTO> create(@RequestBody Invoice invoice) {

        return new ResponseEntity<>(this.invoiceService.create(invoice), HttpStatus.OK);

    }

    @GetMapping("/")
    public ResponseEntity<List<InvoiceDTO>> findAll () {

        return new ResponseEntity<>(this.invoiceService.findAll(), HttpStatus.OK);

    }


    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDTO> one (@PathVariable Integer id) {
        return new ResponseEntity<>(this.invoiceService.findById(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id){
        try {
            this.invoiceService.delete(id);
            return new ResponseEntity<>("El usuario con id: " + id + " ha sido eliminado.", HttpStatus.OK);
        } catch (InvoiceNotFoundException e) {
            return new ResponseEntity<>("No se encontro una factura con ese id.", HttpStatus.NOT_FOUND);
        }
    }




}
