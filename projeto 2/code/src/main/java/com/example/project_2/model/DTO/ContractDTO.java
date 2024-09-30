package com.example.project_2.model.DTO;

import com.example.project_2.Enums.ContractStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Date;

@Component
public class ContractDTO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Schema(description = "Identificador único do contrato", example = "1")
    private Long id;

    @Schema(description = "ID do cliente associado ao contrato", example = "1")
    private Long clientId;

    @Schema(description = "Nome do cliente associado ao contrato", example = "John Doe")
    private String clientName;

    @Schema(description = "ID do proprietário do veículo", example = "2")
    private Long ownerId;

    @Schema(description = "Nome do proprietário do veículo", example = "Jane Smith")
    private String ownerName;

    @Schema(description = "Status atual do contrato", example = "PENDING")
    private ContractStatus status;

    @Schema(description = "Considerações adicionais sobre o contrato", example = "Entrega do veículo às 10h")
    private String considerations;

    @Schema(description = "ID do veículo associado ao contrato", example = "3")
    private Long vehicleId;

    @Schema(description = "Modelo do veículo associado ao contrato", example = "Corolla")
    private String vehicleModel;

    @Schema(description = "Marca do veículo associado ao contrato", example = "Toyota")
    private String vehicleBrand;

    @Schema(description = "Valor total do contrato", example = "1000.00")
    private BigDecimal value;

    @Schema(description = "Data de início do contrato", example = "2023-06-01")
    private Date startDate;

    @Schema(description = "Data de término do contrato", example = "2023-06-07")
    private Date endDate;

    @Schema(description = "ID do agente associado ao contrato", example = "4")
    private Long agentId;

    @Schema(description = "Nome do agente associado ao contrato", example = "Itau")
    private String agentName;

    // Construtores

    public ContractDTO() {
    }

    public boolean updateContract(Long contractId, String status) {
        String sql = "UPDATE contract SET status = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, status, contractId);
        return rowsAffected > 0;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public ContractStatus getStatus() {
        return status;
    }

    public String getStatusString() {
        return status.toString();
    }

    public void setStatus(ContractStatus status) {
        this.status = status;
    }

    public String getConsiderations() {
        return considerations;
    }

    public void setConsiderations(String considerations) {
        this.considerations = considerations;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
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

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }
}
