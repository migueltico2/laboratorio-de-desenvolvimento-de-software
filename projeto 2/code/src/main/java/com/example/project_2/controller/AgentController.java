package com.example.project_2.controller;

import com.example.project_2.model.Agent;
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
@RequestMapping("/api/agents")
@Tag(name = "Agent", description = "API de gerenciamento de agentes")
public class AgentController extends BaseController {

    private static final String DATA_FILE = "users.dat";

    // -------------
    // GET
    // -------------
    @Operation(summary = "Listar todos os agentes", description = "Retorna uma lista de todos os agentes cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação bem-sucedida")
    })
    @GetMapping
    public ResponseEntity<List<Agent>> getAllAgents() {
        List<Agent> agents = users.stream()
                .filter(user -> user instanceof Agent)
                .map(user -> (Agent) user)
                .collect(Collectors.toList());
        return ResponseEntity.ok(agents);
    }

    // -------------
    // POST
    // -------------
    @Operation(summary = "Criar novo agente", description = "Cria um novo agente e retorna o agente criado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Agente criado com sucesso")
    })
    @PostMapping
    public ResponseEntity<Agent> createAgent(@RequestBody Agent agent) {
        Agent newAgent = new Agent(agent.getName(), agent.getEmail(), agent.getPassword());
        users.add(newAgent);
        BaseController.saveUsers(DATA_FILE);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAgent);
    }

    // -------------
    // PUT
    // -------------
    @Operation(summary = "Atualizar agente", description = "Atualiza os campos especificados de um agente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agente atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Falha na atualização do agente"),
            @ApiResponse(responseCode = "404", description = "Agente não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<String> updateAgent(
            @PathVariable Long id,
            @RequestBody Map<String, String> updates,
            @RequestHeader String token) {

        if (!BaseController.isUserLoggedIn(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Erro ao autenticar usuário");
        }

        Optional<User> userOpt = users.stream()
                .filter(u -> u.getId().equals(id) && u instanceof Agent)
                .findFirst();

        if (userOpt.isPresent()) {
            Agent agent = (Agent) userOpt.get();
            Map<String, String> results = new HashMap<>();

            for (Map.Entry<String, String> entry : updates.entrySet()) {
                String attributeName = entry.getKey();
                String newValue = entry.getValue();

                try {
                    String setterMethodName = "set" + capitalize(attributeName);
                    Method setterMethod = Agent.class.getMethod(setterMethodName, String.class);
                    setterMethod.invoke(agent, newValue);
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
