package com.example.project_2.model.DTO;

import com.example.project_2.Enums.VehicleStatus;

public class VehicleDTO {
    private Long id;
    private int ownerId;
    private String registration;
    private int year;
    private String brand;
    private String model;
    private String plate;
    private VehicleStatus status;

    public VehicleDTO() {
    }

    public VehicleDTO(Long id, int ownerId, String registration, int year, String brand, String model, String plate,
            VehicleStatus status) {
        this.id = id;
        this.ownerId = ownerId;
        this.registration = registration;
        this.year = year;
        this.brand = brand;
        this.model = model;
        this.plate = plate;
        this.status = status;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public String getRegistration() {
        return registration;
    }

    public int getYear() {
        return year;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getPlate() {
        return plate;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }
}
