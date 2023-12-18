package com.example.demo.service;

import com.example.demo.exception.ClientNotFoundException;
import com.example.demo.model.Client;
import com.example.demo.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    public Client create (Client newClient) {
        return this.clientRepository.save(newClient);
    }

    public List<Client> findAll() {return this.clientRepository.findAll();}

    public Client findById(Long id) {
        Optional<Client> opClient = this.clientRepository.findById(id);
        if(opClient.isPresent()) {
            return opClient.get();
        } else {
            return new Client();
        }
    }

    public Client update(Client client, Long id) throws ClientNotFoundException {
        Optional<Client> clientDB = this.clientRepository.findById(id);

        Client c = null;

        if(clientDB.isPresent()){
            c = clientDB.get();
            c.setName(client.getName());
            c.setLastName(client.getLastName());
            c.setDocNumber(client.getDocNumber());
            c = this.clientRepository.save(c);
        } else {
            throw new ClientNotFoundException("No se encontro un cliente con ese ID");
        }
        return c;
    }

    public void delete (Long id) throws ClientNotFoundException {
        Optional<Client> clientOptional = this.clientRepository.findById(id);

        if(clientOptional.isPresent()) {
            this.clientRepository.deleteById(id);
        } else {
            throw new ClientNotFoundException("No se encontro un cliente con ese ID");
        }
    }

}
