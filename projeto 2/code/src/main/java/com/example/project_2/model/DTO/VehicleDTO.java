package com.example.project_2.model.DTO;

import com.example.project_2.Enums.VehicleStatus;
import com.example.project_2.model.mapper.ClientMapper;
import com.example.project_2.model.mapper.VehicleMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.util.List;

@Component
public class VehicleDTO {
    private Long id;
    private int ownerId;
    private String registration;
    private int year;
    private String brand;
    private String model;
    private String plate;
    private VehicleStatus status;

    private RowMapper<VehicleDTO> vehicleRowMapper = VehicleMapper.vehicleRowMapper();

    public VehicleDTO() {
    }

    public VehicleDTO(Long id, int ownerId, String registration, int year, String brand, String model, String plate,
            VehicleStatus status) {
        this.id = id;
        this.ownerId = ownerId;
        this.registration = registration;
        this.year = year;
        this.brand = brand;
        this.model = model;
        this.plate = plate;
        this.status = status;
    }

    public boolean addVehicle(VehicleDTO vehicleDTO, Integer userId, JdbcTemplate jdbcTemplate) {
        String insertSql = "INSERT INTO vehicle (registration, year, brand, model, plate, owner_id) VALUES (?, ?, ?, ?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(insertSql,
                vehicleDTO.getRegistration(),
                vehicleDTO.getYear(),
                vehicleDTO.getBrand(),
                vehicleDTO.getModel(),
                vehicleDTO.getPlate(),
                userId);
        return rowsAffected > 0;
    }

    public List<VehicleDTO> getAllVehicles(JdbcTemplate jdbcTemplate) {
        String sql = "SELECT * FROM vehicle";
        return jdbcTemplate.query(sql, vehicleRowMapper);
    }


    public boolean deleteVehicle(Long owner_id, JdbcTemplate jdbcTemplate) {
        String sql = "DELETE FROM vehicle WHERE owner_id = ?";
        return jdbcTemplate.update(sql, owner_id) > 0;
    }

    public Long getId() {
        return id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public String getRegistration() {
        return registration;
    }

    public int getYear() {
        return year;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getPlate() {
        return plate;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }
}
