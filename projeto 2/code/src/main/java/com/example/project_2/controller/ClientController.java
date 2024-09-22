package com.example.project_2.controller;

import com.example.project_2.model.Client;
import com.example.project_2.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
@Tag(name = "Client", description = "API de gerenciamento de clientes")
public class ClientController extends BaseController {

    @Operation(summary = "Criar novo cliente", description = "Cria um novo cliente e retorna o cliente criado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso")
    })
    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        Client newClient = new Client(client.getName(), client.getEmail(), client.getPassword(),
                client.getRG(), client.getCPF(), client.getAddress(), client.getProfession(), client.getEmployer());
        users.add(newClient);
        saveUsers();
        return ResponseEntity.status(HttpStatus.CREATED).body(newClient);
    }

    @Operation(summary = "Atualizar endereço do cliente", description = "Atualiza o endereço de um cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereço atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Falha na atualização do endereço"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @PutMapping("/{id}/update-address")
    public ResponseEntity<String> updateClientAddress(@PathVariable Long id,
            @RequestBody AddressUpdateRequest request) {
        Optional<User> userOpt = users.stream()
                .filter(u -> u.getId().equals(id) && u instanceof Client)
                .findFirst();

        if (userOpt.isPresent()) {
            Client client = (Client) userOpt.get();
            if (client.updateAddress(request.getNewAddress())) {
                saveUsers();
                return ResponseEntity.ok("Endereço atualizado com sucesso");
            } else {
                return ResponseEntity.badRequest().body("Falha na atualização do endereço");
            }
        }
        return ResponseEntity.notFound().build();
    }

    private static class AddressUpdateRequest {
        private String newAddress;

        public String getNewAddress() {
            return newAddress;
        }

        public void setNewAddress(String newAddress) {
            this.newAddress = newAddress;
        }
    }
}
