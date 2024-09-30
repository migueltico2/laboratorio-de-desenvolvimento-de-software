package com.example.project_2.controller;

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

import java.util.List;
import java.util.Map;

import com.example.project_2.model.DTO.ClientDTO;
import com.example.project_2.model.DTO.ContractDTO;
import com.example.project_2.model.DTO.RentalDTO;
import com.example.project_2.model.mapper.ClientMapper;
import com.example.project_2.Util.Auth;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "*")
@Tag(name = "Client", description = "API de gerenciamento de clientes")
public class ClientController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ClientDTO clientDTO;

    @Autowired
    private Auth auth;

    private final RowMapper<ClientDTO> clientRowMapper = ClientMapper.clientRowMapper();

    @Operation(summary = "Listar todos os clientes", description = "Retorna uma lista de todos os clientes cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação bem-sucedida")
    })
    @GetMapping
    public ResponseEntity<List<ClientDTO>> getAllUsers() {
        String sql = "SELECT * FROM client LEFT JOIN app_user ON client.id = app_user.id";
        List<ClientDTO> clients = jdbcTemplate.query(sql, clientRowMapper);
        return ResponseEntity.ok(clients);
    }

    @Operation(summary = "Criar novo cliente", description = "Cria um novo cliente e retorna o cliente criado")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso") })
    @PostMapping
    public ResponseEntity<?> createClient(@RequestBody Client client) {
        try {
            ClientDTO createdClient = clientDTO.createClient(client);
            return ResponseEntity.status(HttpStatus.CREATED).body("Cliente criado com sucesso: " + createdClient);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falha ao criar cliente: " + e.getMessage());
        }
    }

    @Operation(summary = "Atualizar cliente", description = "Atualiza os campos especificados de um cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "403", description = "Não autorizado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateClient(@PathVariable Long id,
            @RequestBody Map<String, Object> updateFields,
            @RequestHeader String token) {
        try {
            if (!auth.authenticate(token)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Erro ao autenticar usuário");
            }

            boolean updated = clientDTO.updateClient(id, updateFields);

            if (updated) {
                return ResponseEntity.ok("Cliente atualizado com sucesso");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar cliente: " + e.getMessage());
        }
    }

}
