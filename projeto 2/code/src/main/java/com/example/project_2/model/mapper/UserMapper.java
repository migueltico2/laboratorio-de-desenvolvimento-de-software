package com.example.project_2.model.mapper;

import com.example.project_2.Enums.VehicleStatus;
import com.example.project_2.model.DTO.UserDTO;
import com.example.project_2.model.DTO.VehicleDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {

    public static RowMapper<UserDTO> baseUserMapper() {
        return (rs, rowNum) -> {
            UserDTO user = new UserDTO();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setUserToken(rs.getString("user_token"));
            return user;
        };
    }

    public static RowMapper<UserDTO> userWithVehiclesMapper() {
        return new RowMapper<UserDTO>() {
            private UserDTO currentUser = null;

            @Override
            public UserDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                Long userId = rs.getLong("id");

                if (currentUser == null || currentUser.getId() != userId) {
                    currentUser = new UserDTO();
                    currentUser.setId(userId);
                    currentUser.setName(rs.getString("name"));
                    currentUser.setEmail(rs.getString("email"));
                    currentUser.setPassword(rs.getString("password"));
                    currentUser.setUserToken(rs.getString("user_token"));
                }

                Long vehicleId = rs.getLong("vehicle.id");
                if (!rs.wasNull()) {
                    VehicleDTO vehicle = new VehicleDTO();
                    vehicle.setId(vehicleId);
                    vehicle.setRegistration(rs.getString("vehicle.registration"));
                    vehicle.setYear(rs.getInt("vehicle.year"));
                    vehicle.setBrand(rs.getString("vehicle.brand"));
                    vehicle.setModel(rs.getString("vehicle.model"));
                    vehicle.setPlate(rs.getString("vehicle.plate"));

                    String status = rs.getString("vehicle.status");
                    if (status != null) {
                        vehicle.setStatus(VehicleStatus.valueOf(status));
                    }

                    currentUser.addVehicle(vehicle);
                }

                return currentUser;
            }
        };
    }
}
