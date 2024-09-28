package com.example.project_2.model;

import com.example.project_2.Enums.VehicleStatus;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

public class Vehicle implements Serializable {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);
    private Long id;
    private int propertaryId;
    private String registration;
    private int year;
    private String brand;
    private String model;
    private String plate;
    private VehicleStatus status;

    public Vehicle(Long id, int propertaryId, String registration, int year, String brand, String model, String plate,
            VehicleStatus status) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.propertaryId = propertaryId;
        this.registration = registration;
        this.year = year;
        this.brand = brand;
        this.model = model;
        this.plate = plate;
        this.status = status;
    }

    public Long getId() {
        return this.id;
    }

    public int getPropertaryId() {
        return this.propertaryId;
    }

    public void setPropertaryId(int propertaryId) {
        this.propertaryId = propertaryId;
    }

    public String getRegistration() {
        return this.registration;
    }

    public int getYear() {
        return this.year;
    }

    public String getBrand() {
        return this.brand;
    }

    public String getModel() {
        return this.model;
    }

    public String getPlate() {
        return this.plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public VehicleStatus getStatus() {
        return this.status;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

}
