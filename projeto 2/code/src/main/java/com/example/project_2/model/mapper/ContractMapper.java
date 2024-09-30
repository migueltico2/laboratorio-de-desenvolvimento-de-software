package com.example.project_2.model.mapper;

import com.example.project_2.model.Contract;
import com.example.project_2.model.DTO.ContractDTO;
import com.example.project_2.Enums.ContractStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.stereotype.Component;

@Component
public class ContractMapper implements RowMapper<ContractDTO> {

    public static RowMapper<ContractDTO> contractRowMapper() {
        return new ContractMapper();
    }

    @Override
    public ContractDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        ContractDTO contract = new ContractDTO();

        contract.setId(rs.getLong("id"));
        contract.setStatus(ContractStatus.valueOf(rs.getString("status")));
        contract.setConsiderations(rs.getString("considerations"));
        contract.setValue(rs.getBigDecimal("value"));
        contract.setStartDate(rs.getDate("start_date"));
        contract.setEndDate(rs.getDate("end_date"));

        // Mapeando o cliente
        contract.setClientId(rs.getLong("client_id"));
        contract.setClientName(rs.getString("client_name"));

        // Mapeando o proprietário
        contract.setOwnerId(rs.getLong("owner_id"));
        contract.setOwnerName(rs.getString("owner_name"));

        // Mapeando o veículo
        contract.setVehicleId(rs.getLong("vehicle_id"));
        contract.setVehicleModel(rs.getString("vehicle_model"));
        contract.setVehicleBrand(rs.getString("vehicle_brand"));

        // Mapeando o agente (se existir)
        Long agentId = rs.getLong("agent_id");
        if (!rs.wasNull()) {
            contract.setAgentId(agentId);
            contract.setAgentName(rs.getString("agent_name"));
        }

        return contract;
    }

    public static ContractDTO toDTO(Contract contract) {
        ContractDTO dto = new ContractDTO();
        dto.setId(contract.getId());
        dto.setStatus(ContractStatus.valueOf(contract.getStatusString()));
        dto.setConsiderations(contract.getConsiderations());
        dto.setValue(contract.getValue());
        dto.setStartDate(contract.getStartDate());
        dto.setEndDate(contract.getEndDate());

        if (contract.getClient() != null) {
            dto.setClientId(contract.getClient().getId());
            dto.setClientName(contract.getClient().getName());
        }

        if (contract.getOwner() != null) {
            dto.setOwnerId(contract.getOwner().getId());
            dto.setOwnerName(contract.getOwner().getName());
        }

        if (contract.getVehicle() != null) {
            dto.setVehicleId(contract.getVehicle().getId());
            dto.setVehicleModel(contract.getVehicle().getModel());
            dto.setVehicleBrand(contract.getVehicle().getBrand());
        }

        if (contract.getAgent() != null) {
            dto.setAgentId(contract.getAgent().getId());
            dto.setAgentName(contract.getAgent().getName());
        }

        return dto;
    }

}
