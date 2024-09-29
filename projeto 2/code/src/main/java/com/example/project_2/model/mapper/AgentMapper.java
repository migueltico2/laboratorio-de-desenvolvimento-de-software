package com.example.project_2.model.mapper;

import com.example.project_2.model.DTO.AgentDTO;
import org.springframework.jdbc.core.RowMapper;

public class AgentMapper {

    public static RowMapper<AgentDTO> agentRowMapper() {
        return (rs, rowNum) -> {
            AgentDTO agent = new AgentDTO();

            // Mapeando atributos da classe User
            agent.setId(rs.getLong("id"));
            agent.setName(rs.getString("name"));
            agent.setEmail(rs.getString("email"));
            agent.setPassword(rs.getString("password"));
            agent.setUserToken(rs.getString("user_token"));

            return agent;
        };
    }
}
