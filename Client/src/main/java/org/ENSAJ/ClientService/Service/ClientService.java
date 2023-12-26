package org.ENSAJ.ClientService.Service;

import org.ENSAJ.ClientService.Model.Client;
import org.ENSAJ.ClientService.Repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    // Afficher la liste des client
    public List<Client> retournerListeClients() {
        return clientRepository.findAll();
    }

    // afficher un client by id
    public Client retournerClientById(Long id) throws Exception {
        return clientRepository.findById(id).orElseThrow(() -> new Exception("Client Introuvable"));
    }

    // pour enregistrer un nouveau client
    public Client enregistrerClient(Client client){
        return clientRepository.save(client);
    }

    // pour modifier un client
    public ResponseEntity<Client> modifierClient(Long id, Client client) throws Exception {
        Client c1 = clientRepository.findById(id).orElseThrow(() -> new Exception("Client Introuvable"));
        c1.setAge(client.getAge());
        c1.setNom(client.getNom());
        clientRepository.save(c1);
        return ResponseEntity.ok().body(c1);
    }

    // pour supprimer un client
    public void supprimerClient(Long id){
        clientRepository.deleteById(id);
    }
}
