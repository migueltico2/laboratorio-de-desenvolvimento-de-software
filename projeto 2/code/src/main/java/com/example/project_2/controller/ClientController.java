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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
@Tag(name = "Client", description = "API de gerenciamento de clientes")
public class ClientController extends BaseController {
    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista de todos os usuários cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação bem-sucedida")
    })
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> clients = users.stream()
                .filter(u -> u instanceof Client)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clients);
    }

    @Operation(summary = "Buscar cliente por ID", description = "Retorna um único cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Optional<User> userOpt = users.stream()
                .filter(u -> u.getId().equals(id) && u instanceof Client)
                .findFirst();

        if (userOpt.isPresent()) {
            return ResponseEntity.ok((Client) userOpt.get());
        }
        return ResponseEntity.notFound().build();
    }

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
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClientAddress(@PathVariable Long id,
            @RequestBody Client newClient) {
        Optional<User> userOpt = users.stream()
                .filter(u -> u.getId().equals(id) && u instanceof Client)
                .findFirst();

        if (userOpt.isPresent()) {
            Client client = (Client) userOpt.get();
            client.updateAddress(newClient.getAddress());
            client.setName(newClient.getName());
            client.setEmail(newClient.getEmail());
            client.setPassword(newClient.getPassword());
            client.setRG(newClient.getRG());
            client.setCPF(newClient.getCPF());
            client.setProfession(newClient.getProfession());
            client.setEmployer(newClient.getEmployer());
            saveUsers();

            return ResponseEntity.ok(client);
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
