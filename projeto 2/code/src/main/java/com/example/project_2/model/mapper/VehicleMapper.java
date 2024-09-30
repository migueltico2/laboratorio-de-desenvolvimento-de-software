package com.example.project_2.model.mapper;

import com.example.project_2.Enums.VehicleStatus;
import com.example.project_2.model.DTO.VehicleDTO;
import org.springframework.jdbc.core.RowMapper;

public class VehicleMapper {

    public static RowMapper<VehicleDTO> vehicleRowMapper() {
        return (rs, rowNum) -> {
            VehicleDTO vehicle = new VehicleDTO();
            vehicle.setId(rs.getLong("id"));
            vehicle.setRegistration(rs.getString("registration"));
            vehicle.setYear(rs.getInt("year"));
            vehicle.setBrand(rs.getString("brand"));
            vehicle.setModel(rs.getString("model"));
            vehicle.setPlate(rs.getString("plate"));
            vehicle.setStatus(VehicleStatus.valueOf(rs.getString("status")));
            return vehicle;
        };
    }
}
