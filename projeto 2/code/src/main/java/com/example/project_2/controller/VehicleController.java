package com.example.project_2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.*;
import com.example.project_2.model.DTO.VehicleDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import com.example.project_2.model.mapper.VehicleMapper;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "*")
@Tag(name = "Vehicle", description = "API de gerenciamento de veículos")
public class VehicleController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private VehicleDTO vehicleDTO;

    @Operation(summary = "Listar todos os veículos", description = "Retorna uma lista de todos os veículos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de veículos retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/all")
    public ResponseEntity<List<VehicleDTO>> getAllVehicles() {
        return ResponseEntity.ok(vehicleDTO.getAllVehicles(jdbcTemplate));
    }
}
