package com.example.project_2.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.example.project_2.model.DTO.UserDTO;

@Component
public class Auth {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Boolean authenticate(String token) {
        try {
            String sql = "SELECT * FROM app_user WHERE user_token = ?::uuid";
            UserDTO user = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                UserDTO dto = new UserDTO();
                dto.setId(rs.getLong("id"));
                dto.setName(rs.getString("name"));
                dto.setEmail(rs.getString("email"));
                dto.setUserToken(rs.getString("user_token"));
                dto.setRole(rs.getString("role"));
                return dto;
            }, token);
            return user != null;
        } catch (Exception e) {
            throw new RuntimeException("Falha na autenticação");
        }
    }

}
