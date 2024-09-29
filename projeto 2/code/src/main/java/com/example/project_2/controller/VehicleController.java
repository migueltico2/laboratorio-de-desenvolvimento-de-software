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

    private final RowMapper<VehicleDTO> vehicleRowMapper = VehicleMapper.vehicleRowMapper();

    @GetMapping
    @Operation(summary = "Listar todos os veículos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veículos listados com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<VehicleDTO>> getAllVehicles() {
        String sql = "SELECT * FROM vehicle";
        List<VehicleDTO> vehicles = jdbcTemplate.query(sql, vehicleRowMapper);
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/available")
    @Operation(summary = "Listar todos os veículos disponíveis para aluguel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veículos listados com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<VehicleDTO>> getAllVehiclesAvaliable() {
        String sql = "SELECT * FROM vehicle WHERE status = 'AVAILABLE'";
        List<VehicleDTO> vehicles = jdbcTemplate.query(sql, vehicleRowMapper);
        return ResponseEntity.ok(vehicles);
    }

}
