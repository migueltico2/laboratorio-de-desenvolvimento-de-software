package com.example.project_2.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.util.List;
import com.example.project_2.model.DTO.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    @Autowired
    private static JdbcTemplate jdbcTemplate;

    private static final RowMapper<UserDTO> userRowMapper = (rs, rowNum) -> {
        UserDTO user = new UserDTO();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setUserToken(rs.getString("user_token"));
        return user;
    };

    public static ResponseEntity<String> authenticateUser(String email, String password) {
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

    public static boolean authenticateToken(String token) {
        if (token.isEmpty()) {
            return false;
        }

        String sql = "SELECT * FROM app_user WHERE user_token = ?";
        List<UserDTO> users = jdbcTemplate.query(sql, userRowMapper, token);
        return !users.isEmpty();

    }

    public static boolean validatePassword(String password, String token) {
        String sql = "SELECT * FROM app_user WHERE user_token = ?";
        List<UserDTO> users = jdbcTemplate.query(sql, userRowMapper, token);
        return users.get(0).getPassword().equals(password);
    }
}
