package com.example.project_2.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.project_2.Enums.ContractStatus;
import com.example.project_2.model.DTO.ContractDTO;
import com.example.project_2.model.DTO.RentalDTO;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import com.example.project_2.Util.Auth;

@RestController
@RequestMapping("/api/contracts")
@CrossOrigin(origins = "*")
@Tag(name = "Contract", description = "API de gerenciamento de contratos")
public class ContractController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Auth auth;
    @Autowired
    private ContractDTO contractDTO;

    @Operation(summary = "Listar todos os contratos do usuário", description = "Retorna uma lista de todos os contratos do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de contratos retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/user")
    public List<ContractDTO> getAllUserContracts(@RequestHeader String token) {
        return contractDTO.getClientContracts(token);
    }

    @Operation(summary = "Atualizar aluguel", description = "Atualiza os campos especificados de um contrato de aluguel existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contrato atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Falha na atualização do contrato"),
            @ApiResponse(responseCode = "403", description = "Erro ao autenticar usuário"),
            @ApiResponse(responseCode = "404", description = "Contrato não encontrado")
    })
    @PutMapping("/update-rental/{id}")
    public ResponseEntity<?> updateRental(@PathVariable Long id, @RequestBody Map<String, String> body,
            @RequestHeader String token) {
        try {

            if (!auth.authenticate(token)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Erro ao autenticar usuário");
            }

            boolean updatedRows = contractDTO.updateContract(id, body.get("status"), body.get("considerations"));

            if (updatedRows) {
                return ResponseEntity.ok("Contrato atualizado com sucesso");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contrato não encontrado");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Status inválido: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falha ao atualizar contrato: " + e.getMessage());
        }
    }

    @Operation(summary = "Solicitar aluguel de veículo", description = "Solicita o aluguel de um veículo para o cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Aluguel solicitado com sucesso")
    })
    @PostMapping("/request-rent")
    public ResponseEntity<String> requestRent(@RequestBody RentalDTO rentalDTO, @RequestHeader String token) {
        try {

            if (!auth.authenticate(token)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token inválido");
            }

            int contract = contractDTO.requestRental(rentalDTO, token);

            if (contract > 0) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Aluguel solicitado com sucesso");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao solicitar aluguel");
            }
        } catch (Exception e) {
            e.printStackTrace(); 
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falha ao solicitar aluguel: " + e.getMessage());
        }
    }

}
