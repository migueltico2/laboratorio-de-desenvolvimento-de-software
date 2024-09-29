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
import java.util.Date;
import com.example.project_2.model.DTO.ClientDTO;
import com.example.project_2.model.mapper.ClientMapper;
import com.example.project_2.util.AuthUtil;
import com.example.project_2.model.DTO.ContractDTO;
import com.example.project_2.model.DTO.RentalDTO;
import com.example.project_2.model.mapper.ContractMapper;

import java.util.Map;
import java.util.ArrayList;

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
    private AuthUtil authUtil;

    private final RowMapper<ClientDTO> clientRowMapper = ClientMapper.clientRowMapper();
    private final RowMapper<ContractDTO> contractRowMapper = ContractMapper.contractRowMapper();

    @Operation(summary = "Listar todos os clientes", description = "Retorna uma lista de todos os clientes cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação bem-sucedida")
    })
    @GetMapping
    public ResponseEntity<List<ClientDTO>> getAllUsers() {
        String sql = "SELECT * FROM client JOIN app_user ON client.user_id = app_user.id";
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
            e.printStackTrace(); // Para logging mais detalhado
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
            if (!authUtil.authenticateToken(token)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Erro ao autenticar usuário");
            }

            String sql = "INSERT INTO contract (considerations, start_date, end_date, value, agent_id, vehicle_id, owner_id, client_id, status) "
                    +
                    "VALUES (?, ?::date, ?::date, ?, ?, ?, " +
                    "(SELECT owner_id FROM vehicle v WHERE v.id = ?), " +
                    "(SELECT c.id FROM client c JOIN app_user u ON c.user_id = u.id WHERE u.user_token = ?::uuid), " +
                    "'PENDING') RETURNING id";
            int contract = jdbcTemplate.update(sql,
                    "",
                    rentalDTO.getStartDate(),
                    rentalDTO.getEndDate(),
                    rentalDTO.getValue(),
                    null,
                    rentalDTO.getVehicleId(),
                    rentalDTO.getVehicleId(),
                    token);

            if (contract > 0) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Aluguel solicitado com sucesso");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao solicitar aluguel");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falha ao solicitar aluguel: " + e.getMessage());
        }
    }

    @Operation(summary = "Verificar aluguéis do cliente", description = "Verifica os aluguéis do cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aluguéis verificados com sucesso")
    })
    @GetMapping("/check-rentals")
    public ResponseEntity<?> checkRentals(@RequestHeader String token) {
        try {
            if (!authUtil.authenticateToken(token)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            String sql = "SELECT * FROM contract WHERE contract.client_id = (SELECT id FROM app_user WHERE user_token = UUID(?))";
            List<ContractDTO> contracts = jdbcTemplate.query(sql, contractRowMapper, token);
            if (contracts.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum aluguel encontrado");
            }
            return ResponseEntity.ok(contracts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falha ao verificar aluguéis: " + e.getMessage());
        }
    }

    @Operation(summary = "Atualizar aluguel", description = "Atualiza os campos especificados de um contrato de aluguel existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contrato atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Falha na atualização do contrato"),
            @ApiResponse(responseCode = "403", description = "Erro ao autenticar usuário"),
            @ApiResponse(responseCode = "404", description = "Contrato não encontrado")
    })
    @PutMapping("/update-rental/{id}")
    public ResponseEntity<String> updateRental(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates,
            @RequestHeader String token) {
        try {
            if (!authUtil.authenticateToken(token)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Erro ao autenticar usuário");
            }

            StringBuilder sqlBuilder = new StringBuilder("UPDATE contract SET ");
            List<Object> params = new ArrayList<>();

            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                sqlBuilder.append(entry.getKey()).append(" = ?, ");
                params.add(entry.getValue());
            }

            // Remove a última vírgula e espaço
            sqlBuilder.setLength(sqlBuilder.length() - 2);
            sqlBuilder.append(" WHERE id = ? AND client_id = (SELECT id FROM app_user WHERE user_token = ?)");

            params.add(id);
            params.add(token);

            int updatedRows = jdbcTemplate.update(sqlBuilder.toString(), params.toArray());

            if (updatedRows > 0) {
                return ResponseEntity.ok("Contrato atualizado com sucesso");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Contrato não encontrado ou você não tem permissão para atualizá-lo");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Falha na atualização do contrato: " + e.getMessage());
        }
    }
    // @Operation(summary = "Atualizar cliente", description = "Atualiza os campos
    // especificados de um cliente existente")
    // @ApiResponses(value = {
    // @ApiResponse(responseCode = "200", description = "Cliente atualizado com
    // sucesso"),
    // @ApiResponse(responseCode = "400", description = "Falha na atualização do
    // cliente"),
    // @ApiResponse(responseCode = "403", description = "Erro ao autenticar
    // usuário"),
    // @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    // })
    // @PutMapping("/{id}")
    // public ResponseEntity<String> updateClient(
    // @PathVariable Long id,
    // @RequestBody Map<String, String> updates,
    // @RequestHeader String token) {

    // if (!BaseController.isUserLoggedIn(token)) {
    // return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Erro ao autenticar
    // usuário");
    // }

    // Optional<User> userOpt = users.stream()
    // .filter(u -> u.getId().equals(id) && u instanceof Client)
    // .findFirst();

    // if (userOpt.isPresent()) {
    // Client client = (Client) userOpt.get();
    // Map<String, String> results = new HashMap<>();

    // for (Map.Entry<String, String> entry : updates.entrySet()) {
    // String attributeName = entry.getKey();
    // String newValue = entry.getValue();

    // try {
    // String setterMethodName = "set" + capitalize(attributeName);
    // Method setterMethod = Client.class.getMethod(setterMethodName, String.class);
    // setterMethod.invoke(client, newValue);
    // results.put(attributeName, "atualizado com sucesso");
    // } catch (NoSuchMethodException e) {
    // results.put(attributeName, "atributo inválido");
    // } catch (Exception e) {
    // results.put(attributeName, "falha na atualização: " + e.getMessage());
    // }
    // }

    // BaseController.saveUsers(DATA_FILE);

    // StringBuilder responseBuilder = new StringBuilder("Resultados da
    // atualização:\n");
    // for (Map.Entry<String, String> result : results.entrySet()) {
    // responseBuilder.append(result.getKey()).append(":
    // ").append(result.getValue()).append("\n");
    // }

    // return ResponseEntity.ok(responseBuilder.toString());
    // }
    // return ResponseEntity.notFound().build();
    // }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
