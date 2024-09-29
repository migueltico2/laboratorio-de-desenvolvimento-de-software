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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import com.example.project_2.model.DTO.ClientDTO;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "*")
@Tag(name = "Client", description = "API de gerenciamento de clientes")
public class ClientController extends BaseController {
    // CAMINHO DO ARQUIVO DE USUÁRIOS passado para o base controller
    private static final String DATA_FILE = "users.dat";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<ClientDTO> clientRowMapper = (rs, rowNum) -> {
        ClientDTO client = new ClientDTO();

        // Mapeando atributos da classe User
        client.setId(rs.getLong("id"));
        client.setName(rs.getString("name"));
        client.setEmail(rs.getString("email"));
        client.setPassword(rs.getString("password"));
        client.setUserToken(rs.getString("user_token"));

        // Mapeando atributos específicos da classe Client
        client.setRG(rs.getString("rg"));
        client.setCPF(rs.getString("cpf"));
        client.setAddress(rs.getString("address"));
        client.setProfession(rs.getString("profession"));
        client.setEmployer(rs.getString("employer"));

        // Mapeando os três últimos salários
        double[] lastThreeSalaries = new double[3];
        lastThreeSalaries[0] = rs.getDouble("salary_one");
        lastThreeSalaries[1] = rs.getDouble("salary_two");
        lastThreeSalaries[2] = rs.getDouble("salary_three");
        client.setLastThreeSalaries(lastThreeSalaries);

        return client;
    };

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

    @Operation(summary = "Buscar cliente por ID", description = "Retorna um único cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Optional<User> userOpt = users.stream()
                .filter(u -> u.getId().equals(id) && u instanceof Client)
                .findFirst();

        if (userOpt.isPresent()) {
            return ResponseEntity.ok((Client) userOpt.get());
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Criar novo cliente", description = "Cria um novo cliente e retorna o cliente criado")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso") })
    @PostMapping
    public ResponseEntity<String> createClient(@RequestBody Client client) {
        try {
            String userSql = "INSERT INTO app_user (name, email, password) VALUES (?, ?, ?) RETURNING id";
            Long userId = jdbcTemplate.queryForObject(userSql, Long.class,
                    client.getName(), client.getEmail(),
                    client.getPassword());

            String clientSql = "INSERT INTO client (rg, cpf, address, profession, employer, user_id, salary_one, salary_two, salary_three) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING *";
            jdbcTemplate.update(
                    clientSql,
                    client.getRG(),
                    client.getCPF(),
                    client.getAddress(),
                    client.getProfession(),
                    client.getEmployer(),
                    userId,
                    client.getLastThreeSalaries()[0],
                    client.getLastThreeSalaries()[1],
                    client.getLastThreeSalaries()[2]);

            return ResponseEntity.status(HttpStatus.CREATED).body("Cliente criado com sucesso");
        } catch (Exception e) {
            e.printStackTrace(); // Para logging mais detalhado
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falha ao criar cliente: " + e.getMessage());
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
