package com.example.project_2.controller;

import com.example.project_2.model.Agent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agents")
@Tag(name = "Agent", description = "API de gerenciamento de agentes")
public class AgentController extends BaseController {

    @Operation(summary = "Criar novo agente", description = "Cria um novo agente e retorna o agente criado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Agente criado com sucesso")
    })
    @PostMapping
    public ResponseEntity<Agent> createAgent(@RequestBody Agent agent) {
        Agent newAgent = new Agent(agent.getName(), agent.getEmail(), agent.getPassword());
        users.add(newAgent);
        saveUsers();
        return ResponseEntity.status(HttpStatus.CREATED).body(newAgent);
    }
}
