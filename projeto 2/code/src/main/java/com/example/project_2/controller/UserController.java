package com.example.project_2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.*;

import com.example.project_2.model.Vehicle;
import com.example.project_2.model.DTO.UserDTO;
import com.example.project_2.model.DTO.VehicleDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import com.example.project_2.util.AuthUtil;
import java.util.List;
import com.example.project_2.model.mapper.UserMapper;
import com.example.project_2.Enums.VehicleStatus;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@Tag(name = "User", description = "API de gerenciamento de usuários")
public class UserController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private UserDTO userDTO;

    private final RowMapper<UserDTO> userRowMapper = UserMapper.userWithVehiclesMapper();
    private final RowMapper<UserDTO> baseUserRowMapper = UserMapper.userRowMapper();

    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista de todos os usuários cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação bem-sucedida")
    })
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userDTO.getAllUsers(jdbcTemplate);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Buscar usuário por ID", description = "Retorna um único usuário pelo seu id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<List<UserDTO>> getUserById(@PathVariable Long id) {
        String sql = "SELECT u.*, " +
                "v.id as \"vehicle.id\", " +
                "v.registration as \"vehicle.registration\", " +
                "v.year as \"vehicle.year\", " +
                "v.brand as \"vehicle.brand\", " +
                "v.model as \"vehicle.model\", " +
                "v.plate as \"vehicle.plate\", " +
                "v.status as \"vehicle.status\" " +
                "FROM app_user u " +
                "LEFT JOIN vehicle v ON u.id = v.owner_id " +
                "WHERE u.id = ?::integer";
        List<UserDTO> users = jdbcTemplate.query(sql, userRowMapper, id);
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

        if (!authUtil.authenticateToken(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Erro ao autenticar usuário");
        }

        String sql = "DELETE FROM app_user WHERE id = ? AND user_token = ?::uuid";
        jdbcTemplate.update(sql, id, token);
        return ResponseEntity.ok("Usuário excluído com sucesso");
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

    @Operation(summary = "Alterar senha do usuário", description = "Altera a senha de um usuário existente")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Senha alterada comsucesso"),
            @ApiResponse(responseCode = "400", description = "Falha na alteração dasenha"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "403", description = "Erro ao autenticarusuário")
    })
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestBody PasswordChangeRequest request,
            @RequestHeader String token) {

        if (!authUtil.authenticateToken(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Erro ao autenticar usuário");
        }

        // TODO: validar se a senha antiga é a mesma do usuário cadastrado

        String sql = "UPDATE app_user SET password = ? WHERE user_token = ?";
        jdbcTemplate.update(sql, baseUserRowMapper, request.getNewPassword(), token);
        return ResponseEntity.ok("Senha alterada com sucesso");
    }

    @Operation(summary = "Adicionar veículo ao usuário", description = "Adiciona um veículo ao usuário")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Veículo adicionado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Erro ao autenticar usuário")
    })
    @PostMapping("/add-vehicle")
    public ResponseEntity<String> addVehicle(@RequestBody VehicleDTO vehicleDTO, @RequestHeader String token) {
        try {
            String userIdSql = "SELECT app_user.id FROM app_user WHERE user_token = ?::uuid";
            Integer userId = jdbcTemplate.queryForObject(userIdSql, Integer.class, token);
            System.out.println("userId: " + userId);
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token inválido ou usuário não encontrado");
            }

            String insertSql = "INSERT INTO vehicle (registration, year, brand, model, plate, status, owner_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
            int rowsAffected = jdbcTemplate.update(insertSql,
                    vehicleDTO.getRegistration(),
                    vehicleDTO.getYear(),
                    vehicleDTO.getBrand(),
                    vehicleDTO.getModel(),
                    vehicleDTO.getPlate(),
                    VehicleStatus.AVAILABLE,
                    userId);

            if (rowsAffected > 0) {
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

    private static class PasswordChangeRequest {
        private String oldPassword;
        private String newPassword;

        public String getOldPassword() {
            return oldPassword;
        }

        public String getNewPassword() {
            return newPassword;
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
