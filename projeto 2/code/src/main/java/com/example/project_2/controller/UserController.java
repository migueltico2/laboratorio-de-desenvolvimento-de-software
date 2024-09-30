package com.example.project_2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.project_2.model.DTO.UserDTO;
import com.example.project_2.model.DTO.VehicleDTO;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import com.example.project_2.Util.Auth;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@Tag(name = "User", description = "API de gerenciamento de usuários")
public class UserController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserDTO userDTO;

    @Autowired
    private Auth auth;

    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista de todos os usuários cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação bem-sucedida")
    })
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userDTO.getAllUsers(jdbcTemplate);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Excluir usuário", description = "Exclui um usuário existente")
    @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Usuário excluído comsucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "403", description = "Erro ao autenticarusuário")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id,
            @RequestHeader String token) {

        if (!auth.authenticate(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Erro ao autenticar usuário");
        }

        boolean deleted = userDTO.deleteUser(id, token);

        if (deleted) {
            return ResponseEntity.ok("Usuário excluído com sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado ou não pôde ser excluído");
        }
    }

    @Operation(summary = "Login de usuário", description = "Realiza o login de um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login bem-sucedido"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            UserDTO user = userDTO.login(loginRequest.getEmail(), loginRequest.getPassword(), jdbcTemplate);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
            }

            return ResponseEntity.ok(user.getUserToken());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao autenticar usuário: " + e.getMessage());
        }
    }

    @Operation(summary = "Adicionar veículo ao usuário", description = "Adiciona um veículo ao usuário")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Veículo adicionado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Erro ao autenticar usuário")
    })
    @PostMapping("/add-vehicle")
    public ResponseEntity<String> addVehicle(@RequestBody VehicleDTO vehicleDTO, @RequestHeader String token) {
        try {
            Integer userId = userDTO.getUserIdByToken(token, jdbcTemplate);
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token inválido ou usuário não encontrado");
            }

            boolean vehicleAdded = vehicleDTO.addVehicle(vehicleDTO, userId, jdbcTemplate);
            if (vehicleAdded) {
                return ResponseEntity.ok("Veículo adicionado com sucesso");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Falha ao adicionar veículo");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao adicionar veículo: " + e.getMessage());
        }
    }

    @Operation(summary = "Atualizar usuário", description = "Atualiza os campos especificados de um usuário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "403", description = "Não autorizado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
            @RequestBody Map<String, Object> updateFields,
            @RequestHeader String token) {
        try {
            if (!auth.authenticate(token)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Erro ao autenticar usuário");
            }

            boolean updated = userDTO.updateUser(id, updateFields);

            if (updated) {
                return ResponseEntity.ok("Usuário atualizado com sucesso");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    private static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

    }
}
