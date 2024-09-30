package com.example.project_2.model.mapper;

import com.example.project_2.model.DTO.ContractDTO;
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
        contract.setStatus(rs.getString("status"));
        contract.setConsiderations(rs.getString("considerations"));
        contract.setValue(rs.getBigDecimal("value"));
        contract.setStartDate(rs.getDate("start_date"));
        contract.setEndDate(rs.getDate("end_date"));

        // Mapeando o cliente

        // Mapeando o proprietário

        // Mapeando o veículo
        contract.setVehicleId(rs.getLong("vehicle_id"));

        return contract;
    }

}
