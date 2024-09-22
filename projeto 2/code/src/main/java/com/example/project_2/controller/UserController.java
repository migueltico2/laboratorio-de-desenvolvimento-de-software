package com.example.project_2.controller;

import com.example.project_2.model.Agent;
import com.example.project_2.model.Client;
import com.example.project_2.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "API de gerenciamento de usuários")
public class UserController {

    private List<User> users;
    private static final String DATA_FILE = "users.dat";

    public UserController() {
        this.users = loadUsers();
    }

    @SuppressWarnings("unchecked")
    private List<User> loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            return (List<User>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista de todos os usuários cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação bem-sucedida")
    })
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Buscar usuário por ID", description = "Retorna um único usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> userOpt = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();

        if (userOpt.isPresent()) {
            return ResponseEntity.ok(userOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Criar novo usuário", description = "Cria um novo usuário e retorna o usuário criado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso")
    })
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User newUser = new User(user.getName(), user.getEmail(), user.getPassword(), null);
        users.add(newUser);
        saveUsers();
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/agent")
    public ResponseEntity<Agent> createAgent(@RequestBody Agent agent) {
        Agent newAgent = new Agent(agent.getName(), agent.getEmail(), agent.getPassword());
        users.add(newAgent);
        saveUsers();
        return ResponseEntity.status(HttpStatus.CREATED).body(newAgent);
    }

    @PostMapping("/client")
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        Client newClient = new Client(client.getName(), client.getEmail(), client.getPassword(),
                client.getRG(), client.getCPF(), client.getAddress(), client.getProfession(), client.getEmployer());
        users.add(newClient);
        saveUsers();
        return ResponseEntity.status(HttpStatus.CREATED).body(newClient);
    }

    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        Optional<User> userOpt = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();

        if (userOpt.isPresent()) {
            User existingUser = userOpt.get();
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            saveUsers();
            return ResponseEntity.ok(existingUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Excluir usuário", description = "Exclui um usuário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean removed = users.removeIf(user -> user.getId().equals(id));
        if (removed) {
            saveUsers();
            return ResponseEntity.noContent().build();
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
        List<User> users = loadUsers();

        Optional<User> userOpt = users.stream()
                .filter(u -> u != null && u.getEmail() != null && u.getEmail().equals(loginRequest.getEmail()))
                .findFirst();

        if (userOpt.isPresent()) {
            User existingUser = userOpt.get();
            if (existingUser.getPassword() != null && existingUser.getPassword().equals(loginRequest.getPassword())) {
                return ResponseEntity.ok("Login bem-sucedido");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
    }

    @Operation(summary = "Alterar senha do usuário", description = "Altera a senha de um usuário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senha alterada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Falha na alteração da senha"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PutMapping("/{id}/change-password")
    public ResponseEntity<String> changePassword(@PathVariable Long id, @RequestBody PasswordChangeRequest request) {
        Optional<User> userOpt = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();

        if (userOpt.isPresent()) {
            User existingUser = userOpt.get();
            if (existingUser.getPassword().equals(request.getOldPassword())) {
                existingUser.setPassword(request.getNewPassword());
                saveUsers();
                return ResponseEntity.ok("Senha alterada com sucesso");
            }
        }
        return ResponseEntity.badRequest().body("Falha na alteração da senha");
    }

    // Classe interna para representar a requisição de mudança de senha
    private static class PasswordChangeRequest {
        private String oldPassword;
        private String newPassword;

        // Getters e setters
        public String getOldPassword() {
            return oldPassword;
        }

        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }

    // Classe interna para representar a requisição de login
    private static class LoginRequest {
        private String email;
        private String password;

        // Getters e setters
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
