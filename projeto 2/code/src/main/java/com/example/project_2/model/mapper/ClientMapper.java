package com.example.project_2.model.mapper;

import com.example.project_2.model.DTO.ClientDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientMapper {

    public static RowMapper<ClientDTO> clientRowMapper() {
        return (rs, rowNum) -> {
            ClientDTO client = new ClientDTO();

            // Mapeando atributos da classe User
            client.setId(rs.getLong("id"));
            client.setName(rs.getString("name"));
            client.setEmail(rs.getString("email"));
            client.setPassword(rs.getString("password"));
            client.setUserToken(rs.getString("user_token"));

            // Mapeando atributos específicos da classe Client
            client.setRG(rs.getString("rg"));
            client.setCPF(rs.getString("cpf"));
            client.setAddress(rs.getString("address"));
            client.setProfession(rs.getString("profession"));
            client.setEmployer(rs.getString("employer"));

            // Mapeando os três últimos salários
            double[] lastThreeSalaries = new double[3];
            lastThreeSalaries[0] = rs.getDouble("salary_one");
            lastThreeSalaries[1] = rs.getDouble("salary_two");
            lastThreeSalaries[2] = rs.getDouble("salary_three");
            client.setLastThreeSalaries(lastThreeSalaries);

            return client;
        };
    }
}
