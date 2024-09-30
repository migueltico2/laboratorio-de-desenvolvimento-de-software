package com.example.project_2.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.project_2.Enums.ContractStatus;
import com.example.project_2.model.DTO.ContractDTO;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import java.util.Map;

@RestController
@RequestMapping("/api/contracts")
@CrossOrigin(origins = "*")
@Tag(name = "Contract", description = "API de gerenciamento de contratos")
public class ContractController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
            System.out.println("Status: " + body.get("status"));
            System.out.println("Considerations: " + body.get("considerations"));
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

}
