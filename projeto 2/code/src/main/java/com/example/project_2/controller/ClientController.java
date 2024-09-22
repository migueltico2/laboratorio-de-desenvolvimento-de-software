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

    @Operation(summary = "Atualizar nome do cliente", description = "Atualiza o nome de um cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nome atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @PutMapping("/{id}/update-name")
    public ResponseEntity<String> updateClientName(@PathVariable Long id, @RequestBody UpdateRequest request) {
        return updateClientAttribute(id, "name", request.getValue());
    }

    @Operation(summary = "Atualizar email do cliente", description = "Atualiza o email de um cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @PutMapping("/{id}/update-email")
    public ResponseEntity<String> updateClientEmail(@PathVariable Long id, @RequestBody UpdateRequest request) {
        return updateClientAttribute(id, "email", request.getValue());
    }

    @Operation(summary = "Atualizar RG do cliente", description = "Atualiza o RG de um cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "RG atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @PutMapping("/{id}/update-rg")
    public ResponseEntity<String> updateClientRG(@PathVariable Long id, @RequestBody UpdateRequest request) {
        return updateClientAttribute(id, "RG", request.getValue());
    }

    @Operation(summary = "Atualizar CPF do cliente", description = "Atualiza o CPF de um cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CPF atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @PutMapping("/{id}/update-cpf")
    public ResponseEntity<String> updateClientCPF(@PathVariable Long id, @RequestBody UpdateRequest request) {
        return updateClientAttribute(id, "CPF", request.getValue());
    }

    @Operation(summary = "Atualizar endereço do cliente", description = "Atualiza o endereço de um cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereço atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @PutMapping("/{id}/update-address")
    public ResponseEntity<String> updateClientAddress(@PathVariable Long id, @RequestBody UpdateRequest request) {
        return updateClientAttribute(id, "address", request.getValue());
    }

    @Operation(summary = "Atualizar profissão do cliente", description = "Atualiza a profissão de um cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profissão atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @PutMapping("/{id}/update-profession")
    public ResponseEntity<String> updateClientProfession(@PathVariable Long id, @RequestBody UpdateRequest request) {
        return updateClientAttribute(id, "profession", request.getValue());
    }

    @Operation(summary = "Atualizar empregador do cliente", description = "Atualiza o empregador de um cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empregador atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @PutMapping("/{id}/update-employer")
    public ResponseEntity<String> updateClientEmployer(@PathVariable Long id, @RequestBody UpdateRequest request) {
        return updateClientAttribute(id, "employer", request.getValue());
    }

    private ResponseEntity<String> updateClientAttribute(Long id, String attributeName, String newValue) {
        Optional<User> userOpt = users.stream()
                .filter(u -> u.getId().equals(id) && u instanceof Client)
                .findFirst();

        if (userOpt.isPresent()) {
            Client client = (Client) userOpt.get();
            try {
                String setterMethodName = "set" + capitalize(attributeName);
                java.lang.reflect.Method setterMethod = Client.class.getMethod(setterMethodName, String.class);
                setterMethod.invoke(client, newValue);
                saveUsers();
                return ResponseEntity.ok(attributeName + " atualizado com sucesso");
            } catch (NoSuchMethodException e) {
                return ResponseEntity.badRequest().body("Atributo inválido: " + attributeName);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Falha na atualização do atributo: " + e.getMessage());
            }
        }
        return ResponseEntity.notFound().build();
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private static class UpdateRequest {
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
