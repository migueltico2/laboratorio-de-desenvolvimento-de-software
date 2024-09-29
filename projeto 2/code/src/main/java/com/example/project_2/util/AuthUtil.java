package com.example.project_2.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.util.List;
import com.example.project_2.model.DTO.UserDTO;
import com.example.project_2.model.mapper.UserMapper;

import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<UserDTO> userRowMapper = UserMapper.baseUserMapper();

    public ResponseEntity<String> authenticateUser(String email, String password) {
        String sql = "SELECT * FROM app_user WHERE email = ?";
        List<UserDTO> users = jdbcTemplate.query(sql, userRowMapper, email);

        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        }

        UserDTO userDTO = users.get(0);
        if (userDTO.getPassword() != null && userDTO.getPassword().equals(password)) {
            return ResponseEntity.ok(userDTO.getUserToken());
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
    }

    public boolean authenticateToken(String token) {
        if (token.isEmpty()) {
            return false;
        }

        String sql = "SELECT * FROM app_user WHERE user_token = UUID(?)";
        List<UserDTO> users = jdbcTemplate.query(sql, userRowMapper, token);
        return !users.isEmpty();

    }

    public boolean validatePassword(String password, String token) {
        String sql = "SELECT * FROM app_user WHERE user_token = ?";
        List<UserDTO> users = jdbcTemplate.query(sql, userRowMapper, token);
        return users.get(0).getPassword().equals(password);
    }
}
