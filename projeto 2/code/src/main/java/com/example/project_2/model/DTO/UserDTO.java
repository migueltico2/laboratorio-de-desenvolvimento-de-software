package com.example.project_2.model.DTO;

import java.util.List;
import java.util.ArrayList;

public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String userToken;
    private String role;
    private List<VehicleDTO> vehicles = new ArrayList<>(); // Inicialize com uma lista vazia

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

    public List<VehicleDTO> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<VehicleDTO> vehicles) {
        this.vehicles = vehicles != null ? vehicles : new ArrayList<>();
    }

    public void addVehicle(VehicleDTO vehicle) {
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
