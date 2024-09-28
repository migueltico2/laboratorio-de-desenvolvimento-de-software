package com.example.project_2.model;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicLong;

public class User implements Serializable {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);
    private static final SecureRandom RANDOM = new SecureRandom();
    private String token;
    private Long id;
    private String name;
    private String email;
    private String password;
    private ArrayList<Vehicle> vehicle;

    private String generateToken() {
        byte[] tokenBytes = new byte[16];
        RANDOM.nextBytes(tokenBytes);
        return Base64.getEncoder().encodeToString(tokenBytes);
    }

    public User(String name, String email, String password, Vehicle vehicle) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.token = generateToken();
        this.name = name;
        this.email = email;
        this.password = password;
        this.vehicle = new ArrayList<Vehicle>();
    }

    public User(String name, String email, String password) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.token = generateToken();
        this.name = name;
        this.email = email;
        this.password = password;
        this.vehicle = new ArrayList<Vehicle>();
    }

    public void addVehicle(Vehicle vehicle) {
        this.vehicle.add(vehicle);
    }

    public User() {
        this.id = ID_GENERATOR.getAndIncrement();
        this.token = generateToken();
        this.vehicle = new ArrayList<Vehicle>();
    }

    public Long getId() {
        return this.id;
    }

    public String getToken() {
        return this.token;
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

    public ArrayList<Vehicle> getVehicle() {
        return this.vehicle;
    }

    public void setVehicle(ArrayList<Vehicle> vehicle) {
        this.vehicle = vehicle;
    }

}
