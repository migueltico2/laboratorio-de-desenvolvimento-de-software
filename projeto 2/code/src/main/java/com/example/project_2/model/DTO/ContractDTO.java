package com.example.project_2.model.DTO;

import com.example.project_2.Enums.ContractStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Component
public class ContractDTO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RowMapper<ContractDTO> contractRowMapper;

    @Schema(description = "Identificador único do contrato", example = "1")
    private Long id;

    @Schema(description = "ID do cliente associado ao contrato", example = "1")
    private String client_id;

    @Schema(description = "Status atual do contrato", example = "PENDING")
    private String status;

    @Schema(description = "Considerações adicionais sobre o contrato", example = "Entrega do veículo às 10h")
    private String considerations;

    @Schema(description = "ID do veículo associado ao contrato", example = "3")
    private Long vehicleId;

    @Schema(description = "Valor total do contrato", example = "1000.00")
    private BigDecimal value;

    @Schema(description = "Data de início do contrato", example = "2023-06-01")
    private Date startDate;

    @Schema(description = "Data de término do contrato", example = "2023-06-07")
    private Date endDate;

    public ContractDTO() {
    }

    public List<ContractDTO> getClientContracts(String token) {

        String clientId = jdbcTemplate.queryForObject(
                "SELECT c.cpf FROM client c JOIN app_user u ON c.id = u.id WHERE u.user_token = ?::uuid",
                String.class, token);

        String sql = "SELECT * FROM contract c WHERE c.client_id = ?";
        return jdbcTemplate.query(sql, contractRowMapper, clientId);
    }

    public boolean updateContract(Long contractId, String status, String considerations) {

        if (status == null) {
            throw new IllegalArgumentException("Status não pode ser nulo");
        }

        String sql = "UPDATE contract SET status = ?::contract_status_enum, considerations = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, status, considerations, contractId);
        return rowsAffected > 0;
    }

    public int requestRental(RentalDTO rentalDTO, String token) {

        String clientId = jdbcTemplate.queryForObject(
                "SELECT c.cpf FROM client c JOIN app_user u ON c.id = u.id WHERE u.user_token = ?::uuid",
                String.class, token);

        String sql = "INSERT INTO contract (considerations, client_id, vehicle_id, start_date, end_date, value) VALUES (?, ?, ?, ?::date, ?::date, ?) RETURNING id";

        return jdbcTemplate.queryForObject(sql, Integer.class,
                "",
                clientId,
                rentalDTO.getVehicleId(),
                rentalDTO.getStartDate(),
                rentalDTO.getEndDate(),
                rentalDTO.getValue());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusString() {
        return status.toString();
    }

    public void setStatus(String status) {
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
}
