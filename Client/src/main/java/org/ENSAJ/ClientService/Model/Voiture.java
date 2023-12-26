package org.ENSAJ.ClientService.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ENSAJ.ClientService.Model.Client;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Voiture {

    private Long Id;
    private String marque;
    private String matricule;
    private String model;
    private Long id_client;
    private Client client;

}
