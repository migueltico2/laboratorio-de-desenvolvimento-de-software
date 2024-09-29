package com.example.project_2.Enums;

public enum VehicleStatus {
    AVAILABLE("Disponível"),
    RENTED("Alugado");

    private final String status;

    VehicleStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
