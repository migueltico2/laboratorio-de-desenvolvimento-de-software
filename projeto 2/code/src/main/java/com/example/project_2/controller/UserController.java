package com.example.project_2.controller;

import com.example.project_2.Enums.VehicleStatus;
import com.example.project_2.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.*;
import com.example.project_2.model.DTO.UserDTO;
import com.example.project_2.model.DTO.VehicleDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import com.example.project_2.util.AuthUtil;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@Tag(name = "User", description = "API de gerenciamento de usuários")
public class UserController extends BaseController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<UserDTO> userRowMapper = (rs, rowNum) -> {
        UserDTO user = new UserDTO();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setUserToken(rs.getString("user_token"));

        // Verificando se há dados de veículo antes de adicionar
        if (rs.getObject("vehicle.id") != null) {
            VehicleDTO vehicle = new VehicleDTO();
            vehicle.setId(rs.getLong("vehicle.id"));
            vehicle.setRegistration(rs.getString("vehicle.registration"));
            vehicle.setYear(rs.getInt("vehicle.year"));
            vehicle.setBrand(rs.getString("vehicle.brand"));
            vehicle.setModel(rs.getString("vehicle.model"));
            vehicle.setPlate(rs.getString("vehicle.plate"));

            String status = rs.getString("vehicle.status");
            if (status != null) {
                vehicle.setStatus(VehicleStatus.valueOf(status));
            }

            user.addVehicle(vehicle);
        }

        return user;
    };

    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista de todos os usuários cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação bem-sucedida")
    })
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        String sql = "SELECT u.*, " +
                "v.id as \"vehicle.id\", " +
                "v.registration as \"vehicle.registration\", " +
                "v.year as \"vehicle.year\", " +
                "v.brand as \"vehicle.brand\", " +
                "v.model as \"vehicle.model\", " +
                "v.plate as \"vehicle.plate\", " +
                "v.status as \"vehicle.status\" " +
                "FROM app_user u " +
                "LEFT JOIN vehicle v ON u.id = v.owner_id";
        List<UserDTO> users = jdbcTemplate.query(sql, userRowMapper);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Buscar usuário por ID", description = "Retorna um único usuário pelo seu id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<List<UserDTO>> getUserById(@PathVariable Long id) {
        String sql = "select * from app_user WHERE id = ? JOIN vehicle on app_user.id = vehicle.owner_id";
        List<UserDTO> users = jdbcTemplate.query(sql, userRowMapper, id);
        return ResponseEntity.ok(users);

    }

    @Operation(summary = "Excluir usuário", description = "Exclui um usuárioexistente")
    @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Usuário excluído comsucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "403", description = "Erro ao autenticarusuário")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id,
            @RequestHeader String token) {

        if (!AuthUtil.authenticateToken(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Erro ao autenticar usuário");
        }

        String sql = "DELETE FROM app_user WHERE id = ?";
        jdbcTemplate.update(sql, id);
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
            String sql = "SELECT * FROM app_user WHERE email = ? AND password = ?";
            List<UserDTO> user = jdbcTemplate.query(sql, userRowMapper, loginRequest.getEmail(),
                    loginRequest.getPassword());

            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
            }

            return ResponseEntity.ok(user.get(0).getUserToken());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao autenticar usuário" + e.getMessage());
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

        if (!AuthUtil.authenticateToken(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Erro ao autenticar usuário");
        }

        // TODO: validar se a senha antiga é a mesma do usuário cadastrado

        String sql = "UPDATE app_user SET password = ? WHERE user_token = ?";
        jdbcTemplate.update(sql, request.getNewPassword(), token);
        return ResponseEntity.ok("Senha alterada com sucesso");
    }

    @Operation(summary = "Adicionar veículo ao usuário", description = "Adiciona um veículo ao usuário")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Veículo adicionado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Erro ao autenticar usuário")
    })
    @PostMapping("/add-vehicle")
    public ResponseEntity<String> addVehicle(@RequestBody VehicleDTO vehicleDTO, @RequestHeader String token) {
        if (!AuthUtil.authenticateToken(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Erro ao autenticar usuário");
        }

        String sql = "INSERT into vehicle (registration, year, brand, model, plate, status, owner_id) VALUES (?, ?, ?, ?, ?, ?, (SELECT id from app_user where user_token = ?)";
        jdbcTemplate.update(sql, vehicleDTO.getRegistration(), vehicleDTO.getYear(), vehicleDTO.getBrand(),
                vehicleDTO.getModel(), vehicleDTO.getPlate(), vehicleDTO.getStatus(), token);
        return ResponseEntity.ok("Veículo adicionado com sucesso");
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
