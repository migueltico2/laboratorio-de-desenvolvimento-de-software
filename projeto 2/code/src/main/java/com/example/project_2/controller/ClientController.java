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
import com.example.project_2.model.DTO.ClientDTO;
import com.example.project_2.model.DTO.ContractDTO;
import com.example.project_2.model.DTO.RentalDTO;
import com.example.project_2.model.mapper.ClientMapper;

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
    private ContractDTO contractDTO;

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

    @Operation(summary = "Solicitar aluguel de veículo", description = "Solicita o aluguel de um veículo para o cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Aluguel solicitado com sucesso")
    })
    @PostMapping("/request-rent")
    public ResponseEntity<String> requestRent(@RequestBody RentalDTO rentalDTO, @RequestHeader String token) {
        try {

            int contract = contractDTO.requestRental(rentalDTO, token);

            if (contract > 0) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Aluguel solicitado com sucesso");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao solicitar aluguel");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Para logging mais detalhado
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falha ao solicitar aluguel: " + e.getMessage());
        }
    }

}
