package com.example.project_2.model.DTO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.example.project_2.model.Vehicle;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String userToken;
    private String role;
    private List<Vehicle> vehicles = new ArrayList<>();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RowMapper<UserDTO> baseUserRowMapper;

    @Autowired
    private ContractDTO contractDTO;

    @Autowired
    private VehicleDTO vehicleDTO;

    @Autowired
    private ClientDTO clientDTO;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserDTO login(String email, String password, JdbcTemplate jdbcTemplate) {
        String sql = "SELECT * FROM app_user WHERE email = ? AND password = ?";
        List<UserDTO> users = jdbcTemplate.query(sql, baseUserRowMapper, email, password);
        return users.isEmpty() ? null : users.get(0);
    }

    public List<UserDTO> getAllUsers(JdbcTemplate jdbcTemplate) {
        String sql = "SELECT * FROM app_user";
        return jdbcTemplate.query(sql, baseUserRowMapper);
    }

    public Integer getUserIdByToken(String token, JdbcTemplate jdbcTemplate) {
        String userIdSql = "SELECT app_user.id FROM app_user WHERE user_token = ?::uuid";
        try {
            return jdbcTemplate.queryForObject(userIdSql, Integer.class, token);
        } catch (Exception e) {
            return null;
        }
    }

    public UserDTO(Long id, String name, String email, String userToken, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.userToken = userToken;
        this.role = role;
    }

    public UserDTO() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserToken() {
        return this.userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles != null ? vehicles : new ArrayList<>();
    }

    public void addVehicle(Vehicle vehicle) {
        if (vehicle != null) {
            this.vehicles.add(vehicle);
        }
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean deleteUser(Long id, String token) {
        vehicleDTO.deleteVehicle(id, jdbcTemplate);
        clientDTO.deleteClient(id);

        String sql = "DELETE FROM app_user WHERE id = ? AND user_token = ?::uuid";
        int rowsAffected = jdbcTemplate.update(sql, id, token);
        return rowsAffected > 0;
    }

    public boolean changePassword(String newPassword, String token) {
        String sql = "UPDATE app_user SET password = ? WHERE user_token = ?::uuid";
        int rowsAffected = jdbcTemplate.update(sql, newPassword, token);
        return rowsAffected > 0;
    }

    public boolean updateUser(Long userId, Map<String, Object> fields) {
        if (fields == null || fields.isEmpty()) {
            throw new IllegalArgumentException("Nenhum campo fornecido para atualização");
        }

        Map<String, Object> nonNullFields = fields.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (nonNullFields.isEmpty()) {
            throw new IllegalArgumentException("Todos os campos fornecidos são nulos");
        }

        StringBuilder sqlBuilder = new StringBuilder("UPDATE app_user SET ");
        sqlBuilder.append(nonNullFields.keySet().stream()
                .map(key -> key + " = :" + key)
                .collect(Collectors.joining(", ")));
        sqlBuilder.append(" WHERE id = :id");

        nonNullFields.put("id", userId);

        MapSqlParameterSource parameters = new MapSqlParameterSource(nonNullFields);

        int rowsAffected = namedParameterJdbcTemplate.update(sqlBuilder.toString(), parameters);

        return rowsAffected > 0;
    }
}
