package com.example.project_2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import java.math.BigDecimal;

public class Contract {
    @Schema(name = "id", description = "Identificador único do contrato", example = "1")
    private Long id;

    @Schema(name = "client", description = "Cliente associado ao contrato", example = "John Doe")
    private Client client;

    @Schema(name = "owner", description = "Proprietário do veículo", example = "Jane Smith")
    private User owner;

    @Schema(name = "status", description = "Status atual do contrato", example = "PENDING")
    private ContractStatus status;

    @Schema(name = "considerations", description = "Considerações adicionais sobre o contrato", example = "Entrega do veículo às 10h")
    private String considerations;

    @Schema(name = "vehicle", description = "Veículo associado ao contrato", example = "Toyota Corolla 2022")
    private Vehicle vehicle;

    @Schema(name = "value", description = "Valor total do contrato", example = "1000.00")
    private BigDecimal value;

    @Schema(name = "start_date", description = "Data de início do contrato", example = "2023-06-01")
    private Date startDate;

    @Schema(name = "end_date", description = "Data de término do contrato", example = "2023-06-07")
    private Date endDate;

    @Schema(name = "agent", description = "Agente associado ao contrato", example = "Itau")
    private Agent agent;

    public Contract() {
        this.status = ContractStatus.PENDING;
    }

    public Contract(Long id, Client client, User owner, Vehicle vehicle, BigDecimal value, Date startDate,
            Date endDate) {
        this.id = id;
        this.client = client;
        this.owner = owner;
        this.vehicle = vehicle;
        this.value = value;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = ContractStatus.PENDING;
        this.considerations = "";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public ContractStatus getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = ContractStatus.valueOf(status);
    }

    public String getStatusString() {
        return status.toString();
    }

    public String getConsiderations() {
        return considerations;
    }

    public void setConsiderations(String considerations) {
        this.considerations = considerations;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }
}

enum ContractStatus {
    PENDING,
    APPROVED,
    REJECTED
}
