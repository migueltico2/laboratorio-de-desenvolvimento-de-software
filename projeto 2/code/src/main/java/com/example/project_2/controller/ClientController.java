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
    // CAMINHO DO ARQUIVO DE USUÁRIOS passado para o base controller
    private static final String DATA_FILE = "users.dat";

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
        saveUsers(DATA_FILE);
        return ResponseEntity.status(HttpStatus.CREATED).body(newClient);
    }

    @Operation(summary = "Atualizar cliente", description = "Atualiza os campos especificados de um cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Falha na atualização do cliente"),
            @ApiResponse(responseCode = "403", description = "Erro ao autenticar usuário"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<String> updateClient(
            @PathVariable Long id,
            @RequestBody Map<String, String> updates,
            @RequestHeader String token) {

        if (!BaseController.isUserLoggedIn(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Erro ao autenticar usuário");
        }

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

            BaseController.saveUsers(DATA_FILE);

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
