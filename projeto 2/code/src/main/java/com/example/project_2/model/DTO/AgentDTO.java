package com.example.project_2.model.DTO;

public class AgentDTO extends UserDTO {
    public AgentDTO() {
        super();
    }

    public AgentDTO(Long id, String name, String email, String user_token) {
        super(id, name, email, user_token);
    }
}
