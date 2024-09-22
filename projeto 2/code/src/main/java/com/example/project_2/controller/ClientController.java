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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
@Tag(name = "Client", description = "API de gerenciamento de clientes")
public class ClientController extends BaseController {

    @Operation(summary = "Listar todos os clientes", description = "Retorna uma lista de todos os clientes cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação bem-sucedida")
    })
    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = users.stream()
                .filter(user -> user instanceof Client)
                .map(user -> (Client) user)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clients);
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

    @Operation(summary = "Atualizar cliente", description = "Atualiza os campos especificados de um cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Falha na atualização do cliente"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<String> updateClient(@PathVariable Long id, @RequestBody Map<String, String> updates) {
        Optional<User> userOpt = users.stream()
                .filter(u -> u.getId().equals(id) && u instanceof Client)
                .findFirst();

        if (userOpt.isPresent()) {
            Client client = (Client) userOpt.get();
            Map<String, String> results = new HashMap<>();

            for (Map.Entry<String, String> entry : updates.entrySet()) {
                String attributeName = entry.getKey();
                String newValue = entry.getValue();

                try {
                    String setterMethodName = "set" + capitalize(attributeName);
                    Method setterMethod = Client.class.getMethod(setterMethodName, String.class);
                    setterMethod.invoke(client, newValue);
                    results.put(attributeName, "atualizado com sucesso");
                } catch (NoSuchMethodException e) {
                    results.put(attributeName, "atributo inválido");
                } catch (Exception e) {
                    results.put(attributeName, "falha na atualização: " + e.getMessage());
                }
            }

            saveUsers();

            StringBuilder responseBuilder = new StringBuilder("Resultados da atualização:\n");
            for (Map.Entry<String, String> result : results.entrySet()) {
                responseBuilder.append(result.getKey()).append(": ").append(result.getValue()).append("\n");
            }

            return ResponseEntity.ok(responseBuilder.toString());
        }
        return ResponseEntity.notFound().build();
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
