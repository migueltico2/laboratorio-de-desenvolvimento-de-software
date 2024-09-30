package com.example.project_2.model.mapper;

import com.example.project_2.model.DTO.ClientDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public static RowMapper<ClientDTO> clientRowMapper() {
        return (rs, rowNum) -> {
            ClientDTO client = new ClientDTO();

            client.setId(rs.getLong("id"));
            client.setName(rs.getString("name"));
            client.setEmail(rs.getString("email"));
            client.setUserToken(rs.getString("user_token"));

            client.setRG(rs.getString("rg"));
            client.setCPF(rs.getString("cpf"));
            client.setAddress(rs.getString("address"));
            client.setProfession(rs.getString("profession"));
            client.setEmployer(rs.getString("employer"));

            client.setLastThreeSalaries(rs.getDouble("last_three_salaries"));

            return client;
        };
    }
}
