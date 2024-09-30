package com.example.project_2.model.DTO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.example.project_2.model.User;
import com.example.project_2.model.Vehicle;
import com.example.project_2.model.mapper.UserMapper;

import java.util.ArrayList;
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

    public UserDTO login(String email, String password) {
        String sql = "SELECT * FROM app_user WHERE email = ? AND password = ?";
        List<UserDTO> users = jdbcTemplate.query(sql, baseUserRowMapper, email, password);
        return users.isEmpty() ? null : users.get(0);
    }

    public List<UserDTO> getAllUsers() {
        String sql = "SELECT * FROM app_user";
        return jdbcTemplate.query(sql, baseUserRowMapper);
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
}
