package com.example.project_2.controller;

import com.example.project_2.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "API de gerenciamento de usuários")
public class UserController extends BaseController {

    // CAMINHO DO ARQUIVO DE USUÁRIOS passado para o base controller
    private static final String DATA_FILE = "users.dat";

    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista de todos os usuários cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação bem-sucedida")
    })
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(BaseController.users);
    }

    @Operation(summary = "Buscar usuário por ID", description = "Retorna um único usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> userOpt = BaseController.users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();

        if (userOpt.isPresent()) {
            return ResponseEntity.ok(userOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Excluir usuário", description = "Exclui um usuário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "403", description = "Erro ao autenticar usuário")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id, @RequestHeader String token) {

        if (!BaseController.isUserLoggedIn(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Erro ao autenticar usuário");
        }

        boolean removed = BaseController.users.removeIf(user -> user.getId().equals(id));
        if (removed) {
            BaseController.saveUsers(DATA_FILE);
            return ResponseEntity.ok("Usuário excluído com sucesso");
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Login de usuário", description = "Realiza o login de um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login bem-sucedido"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        List<User> users = BaseController.loadUsers(DATA_FILE);

        Optional<User> userOpt = users.stream()
                .filter(u -> u != null && u.getEmail() != null && u.getEmail().equals(loginRequest.getEmail()))
                .findFirst();

        if (userOpt.isPresent()) {
            User existingUser = userOpt.get();
            if (existingUser.getPassword() != null && existingUser.getPassword().equals(loginRequest.getPassword())) {
                return ResponseEntity.ok(existingUser.getToken());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
    }

    @Operation(summary = "Alterar senha do usuário", description = "Altera a senha de um usuário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senha alterada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Falha na alteração da senha"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "403", description = "Erro ao autenticar usuário")
    })
    @PutMapping("/{id}/change-password")
    public ResponseEntity<String> changePassword(@PathVariable Long id, @RequestBody PasswordChangeRequest request,
            @RequestHeader String token) {

        if (!BaseController.isUserLoggedIn(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Erro ao autenticar usuário");
        }

        Optional<User> userOpt = BaseController.users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();

        if (userOpt.isPresent()) {
            User existingUser = userOpt.get();
            if (existingUser.getPassword().equals(request.getOldPassword())) {
                existingUser.setPassword(request.getNewPassword());
                BaseController.saveUsers(DATA_FILE);
                return ResponseEntity.ok("Senha alterada com sucesso");
            } else {
                return ResponseEntity.badRequest().body("Senha antiga inválida");
            }
        }
        return ResponseEntity.notFound().build();
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
