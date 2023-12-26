package org.ENSAJ.ClientService.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ENSAJ.ClientService.ClientApplication;
import org.ENSAJ.ClientService.Model.Client;
import org.ENSAJ.ClientService.Model.Voiture;
import org.ENSAJ.ClientService.Service.ClientService;
//import org.ENSAJ.ClientService.Model.Voiture;
import org.ENSAJ.ClientService.consumer.RabbitMQConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@RestController
public class ClientController {


    @FeignClient(name="SERVICE-VOITURE")
    interface VoitureService{
        @GetMapping(path="/voitures/client/{id}")
        public  List<Voiture> voitureByClientId(@PathVariable Long id);
    }

    @Autowired
    VoitureService voitureService;
    @Autowired
    ClientService clientService;

    // Autowire RabbitTemplate for RabbitMQ operations
    @Autowired
    private AmqpTemplate rabbitTemplate;
    private List<Voiture> receivedVoitures = new ArrayList<>();

    private static Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @GetMapping("/clients")
    public List<Client> chercherClient(){
        return clientService.retournerListeClients();
    }


    @GetMapping("/clients/{id}")
    public Client chercherUnClient(@PathVariable Long id) throws Exception {
        return clientService.retournerClientById(id);
    }

    @PostMapping("/clients")
    public Client enregistrerUnClient(@RequestBody Client client){
        return clientService.enregistrerClient(client);
    }

    @PutMapping("/clients/{id}")
    public ResponseEntity<Client> modifierUnClient(@PathVariable Long id, @RequestBody Client client) throws Exception {
        return clientService.modifierClient(id, client);
    }

    @DeleteMapping("/clients/{id}")
    public void deleteUnClient(@PathVariable Long id){
        clientService.supprimerClient(id);
    }

    @RabbitListener(queues = {"microservice_queue"})
    public void consume(String message) {
        LOGGER.info(String.format("Received message -> %s", message));
        try {
            List<Voiture> voitures = parseJsonToList(message);
            receivedVoitures = voitures;
            LOGGER.info("Voitures received -> {}", receivedVoitures);
        } catch (Exception e) {
            LOGGER.error("Error processing message", e);
        }
    }

    private List<Voiture> parseJsonToList(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, new TypeReference<List<Voiture>>() {});
        } catch (JsonProcessingException e) {
            LOGGER.error("Error parsing JSON", e);
            return Collections.emptyList();
        }
    }

    @GetMapping("/client/voitures")
    public  ResponseEntity<List<Voiture>> chercherLesVoitures() {
        System.out.println(receivedVoitures);
        System.out.println("FATY");
        return ResponseEntity.ok(receivedVoitures);
    }


}
