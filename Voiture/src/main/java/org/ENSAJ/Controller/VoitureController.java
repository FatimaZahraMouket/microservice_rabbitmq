package org.ENSAJ.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ENSAJ.Model.Voiture;
import org.ENSAJ.Repository.VoitureRepository;
import org.ENSAJ.Service.VoitureService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VoitureController {

    @Autowired
    VoitureRepository voitureRepository;

    @Autowired
    VoitureService voitureService;
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @GetMapping(value = "/voitures", produces = {"application/json"})
    public ResponseEntity<List<Voiture>> chercherVoiture() {
        List<Voiture> voitures = voitureRepository.findAll();

        String jsonVoitures = convertListToJson(voitures);

        rabbitTemplate.convertAndSend("microservice_exchange", "microservice_key", jsonVoitures);

        return ResponseEntity.ok(voitures);
    }

    private String convertListToJson(List<Voiture> voitures) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(voitures);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    @GetMapping(value ="/voitures/client/{id}", produces = {"application/json"})
    public ResponseEntity<List<Voiture>> chercherVoitureByClientId(@PathVariable Long id){
        return ResponseEntity.ok(voitureRepository.findByClientId(id));
    }

    @GetMapping("/voitures/{Id}")
    public Voiture chercherUneVoiture(@PathVariable Long Id) throws Exception{
        return voitureRepository.findById(Id).orElseThrow(() -> new Exception("Voiture Introuvable"));
    }

//    @GetMapping("/voitures/client/{Id}")
//    public List<Voiture> chercherVoitureParClient(@PathVariable Long Id){
//        return voitureRepository.findByClientId(Id);
//    }

    @PostMapping("/voitures")
    public Voiture enregistrerUneVoiture(@RequestBody Voiture voiture){
        return voitureService.enregistrerVoiture(voiture);
    }



}
