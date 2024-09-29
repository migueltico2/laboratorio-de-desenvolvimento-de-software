package com.example.project_2.model;

import io.swagger.v3.oas.annotations.media.Schema;

public class Client extends User {

    @Schema(description = "RG do cliente", example = "18983332")
    private String RG;

    @Schema(description = "CPF do cliente", example = "123.456.789-00")
    private String CPF;

    @Schema(description = "Endereço do cliente", example = "Rua Example, 123")
    private String address;

    @Schema(description = "Profissão do cliente", example = "Engenheiro")
    private String profession;

    @Schema(description = "Empregador do cliente", example = "Empresa XYZ")
    private String employer;

    @Schema(description = "Últimos três salários do cliente", example = "[5000.00, 5200.00, 5500.00]")
    private double[] lastThreeSalaries;

    public Client(String name, String email, String password, String RG, String CPF, String address, String profession,
            String employer, String role) {
        super(null, name, email, password, null, role);
        this.RG = RG;
        this.CPF = CPF;
        this.address = address;
        this.profession = profession;
        this.employer = employer;
        this.lastThreeSalaries = new double[3];
    }

    public Client(Long id, String name, String email, String password, String RG, String CPF, String address,
            String profession, String employer, String role) {
        super(id, name, email, password, null, role);
        this.RG = RG;
        this.CPF = CPF;
        this.address = address;
        this.profession = profession;
        this.employer = employer;
        this.lastThreeSalaries = new double[3];
    }

    public Client() {
        super();
    }

    public Client(String name, String email, String password, String role) {
        super(null, name, email, password, null, role);
    }

    public void requestRent(Vehicle vehicle) {
    }

    public void modifyRentRequest(Vehicle vehicle) {
    }

    public void cancelRentRequest(Vehicle vehicle) {
    }

    public void searchRequest(Vehicle vehicle) {
    }

    public String getRG() {
        return RG;
    }

    public void setRG(String RG) {
        this.RG = RG;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public double[] getLastThreeSalaries() {
        return lastThreeSalaries;
    }

    public void setLastThreeSalaries(double[] lastThreeSalaries) {
        this.lastThreeSalaries = lastThreeSalaries;
    }

    public boolean updateAddress(String newAddress) {
        if (newAddress != null && !newAddress.trim().isEmpty()) {
            this.address = newAddress.trim();
            return true;
        }
        return false;
    }

    public User getUser() {
        return super.getUser();
    }

    public String getUserToken() {
        return super.getUserToken();
    }

    public void setUserToken(String userToken) {
        super.setUserToken(userToken);
    }
}
