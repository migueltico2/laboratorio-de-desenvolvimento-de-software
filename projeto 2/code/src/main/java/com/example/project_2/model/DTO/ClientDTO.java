package com.example.project_2.model.DTO;

import com.example.project_2.model.Client;

import io.swagger.v3.oas.annotations.media.Schema;

public class ClientDTO extends UserDTO {

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

    public ClientDTO() {
        super();
    }

    public ClientDTO(Long id, String name, String email, String user_token, String RG, String CPF, String address,
            String profession, String employer, double[] lastThreeSalaries) {
        super(id, name, email, user_token);
        this.RG = RG;
        this.CPF = CPF;
        this.address = address;
        this.profession = profession;
        this.employer = employer;
        this.lastThreeSalaries = lastThreeSalaries;
    }

    // Getters e Setters

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

    // Métodos de conversão

    public static ClientDTO fromClient(Client client) {
        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setName(client.getName());
        dto.setEmail(client.getEmail());
        dto.setUserToken(client.getUserToken());
        dto.setRG(client.getRG());
        dto.setCPF(client.getCPF());
        dto.setAddress(client.getAddress());
        dto.setProfession(client.getProfession());
        dto.setEmployer(client.getEmployer());
        dto.setLastThreeSalaries(client.getLastThreeSalaries());
        return dto;
    }

    public Client toClient() {
        Client client = new Client();
        client.setId(this.getId());
        client.setName(this.getName());
        client.setEmail(this.getEmail());
        client.setUserToken(this.getUserToken());
        client.setRG(this.getRG());
        client.setCPF(this.getCPF());
        client.setAddress(this.getAddress());
        client.setProfession(this.getProfession());
        client.setEmployer(this.getEmployer());
        client.setLastThreeSalaries(this.getLastThreeSalaries());
        return client;
    }
}
