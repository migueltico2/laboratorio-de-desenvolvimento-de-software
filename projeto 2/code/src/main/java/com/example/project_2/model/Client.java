package com.example.project_2.model;

public class Client extends User {
    private String RG;
    private String CPF;
    private String address;
    private String profession;
    private String employer;
    private double[] lastThreeSalaries;

    public Client(String name, String email, String password, String RG, String CPF, String address, String profession,
            String employer) {
        super(name, email, password, null);
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

    public Client(String name, String email, String password) {
        super(name, email, password, null);
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
}
