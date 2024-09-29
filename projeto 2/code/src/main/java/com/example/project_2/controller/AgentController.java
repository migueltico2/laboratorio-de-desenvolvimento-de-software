package com.example.project_2.controller;

import com.example.project_2.model.Agent;
import com.example.project_2.model.Client;
import com.example.project_2.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import com.example.project_2.model.DTO.AgentDTO;
import com.example.project_2.model.mapper.AgentMapper;

@RestController
@RequestMapping("/api/agents")
@CrossOrigin(origins = "*")
@Tag(name = "Agent", description = "API de gerenciamento de agentes")
public class AgentController {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final RowMapper<AgentDTO> agentRowMapper = AgentMapper.agentRowMapper();

    @Operation(summary = "Listar todos os agentes", description = "Retorna uma lista de todos os agentes cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação bem-sucedida")
    })
    @GetMapping
    public ResponseEntity<List<AgentDTO>> getAllAgents() {
        String sql = "SELECT * FROM agent JOIN app_user ON agent.user_id = app_user.id";
        List<AgentDTO> agents = jdbcTemplate.query(sql, agentRowMapper);
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
    public ResponseEntity<String> createAgent(@RequestBody Agent agent) {
        String userSql = "INSERT INTO app_user (name, email, password, role) VALUES (?, ?, ?, ?) RETURNING id";
        Long userId = jdbcTemplate.queryForObject(userSql, Long.class, agent.getName(), agent.getEmail(),
                agent.getPassword(), "AGENT");

        String agentSql = "INSERT INTO agent (user_id) VALUES (?)";
        jdbcTemplate.update(agentSql, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body("Agente criado com sucesso");
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

        // Verificar autenticação
        String checkTokenSql = "SELECT COUNT(*) FROM app_user WHERE token = ?";
        Integer count = jdbcTemplate.queryForObject(checkTokenSql, Integer.class, token);
        if (count == null || count == 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Erro ao autenticar usuário");
        }

        // Verificar se o agente existe
        String checkAgentSql = "SELECT COUNT(*) FROM agent WHERE user_id = ?";
        count = jdbcTemplate.queryForObject(checkAgentSql, Integer.class, id);
        if (count == null || count == 0) {
            return ResponseEntity.notFound().build();
        }

        StringBuilder updateSql = new StringBuilder("UPDATE app_user SET ");
        List<Object> params = new ArrayList<>();
        Map<String, String> results = new HashMap<>();

        for (Map.Entry<String, String> entry : updates.entrySet()) {
            String attributeName = entry.getKey();
            String newValue = entry.getValue();

            // Verificar se o atributo é válido
            if (isValidAttribute(attributeName)) {
                updateSql.append(attributeName).append(" = ?, ");
                params.add(newValue);
                results.put(attributeName, "atualizado com sucesso");
            } else {
                results.put(attributeName, "atributo inválido");
            }
        }

        // Remover a última vírgula e espaço
        updateSql.setLength(updateSql.length() - 2);
        updateSql.append(" WHERE id = ?");
        params.add(id);

        try {
            int rowsAffected = jdbcTemplate.update(updateSql.toString(), params.toArray());
            if (rowsAffected > 0) {
                StringBuilder responseBuilder = new StringBuilder("Resultados da atualização:\n");
                for (Map.Entry<String, String> result : results.entrySet()) {
                    responseBuilder.append(result.getKey()).append(": ").append(result.getValue()).append("\n");
                }
                return ResponseEntity.ok(responseBuilder.toString());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar o agente: " + e.getMessage());
        }
    }

    private boolean isValidAttribute(String attributeName) {
        // Lista de atributos válidos
        List<String> validAttributes = Arrays.asList("name", "email", "password");
        return validAttributes.contains(attributeName.toLowerCase());
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
