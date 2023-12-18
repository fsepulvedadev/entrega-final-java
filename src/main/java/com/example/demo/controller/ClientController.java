package com.example.demo.controller;


import com.example.demo.exception.ClientNotFoundException;
import com.example.demo.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.ClientService;

import java.util.List;

@RestController
@RequestMapping(path = "api/client")
public class ClientController {

    @Autowired
    private ClientService clientService;


    // CREAR NUEVO CLIENT
    @PostMapping("/")
    public ResponseEntity<Client> create(@RequestBody Client client) {
        return new ResponseEntity<>(this.clientService.create(client), HttpStatus.OK);
    }
    // TRAER UN CLIENT
    @GetMapping("/{id}")
    public Client one(@PathVariable Long id) {
        return this.clientService.findById(id);
    }

    // TRAER TODOS LOS CLIENTS
    @GetMapping("/")
    public ResponseEntity<List<Client>> findAll() {
        return new ResponseEntity<>(this.clientService.findAll(), HttpStatus.OK);}


    // BORRAR UN CLIENT
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        try {
            this.clientService.delete(id);
            return new ResponseEntity<>("El usuario con id: " + id + " ha sido eliminado.", HttpStatus.OK);
        } catch (ClientNotFoundException e) {

            return new ResponseEntity<>("No se encontraron clientes con el id: " + id + ".", HttpStatus.NOT_FOUND);

        }

    }

    // EDITAR UN CLIENT


    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody Client client) {

        try {
            this.clientService.update(client, id);
            return new ResponseEntity<>("El cliente con id: " + id + " ha sido editado con exito.", HttpStatus.OK);
        } catch (ClientNotFoundException e) {
            Client c = new Client();

            return new ResponseEntity<>("No se encontro un usuario con ese ID", HttpStatus.NOT_FOUND);
        }
    }
}
