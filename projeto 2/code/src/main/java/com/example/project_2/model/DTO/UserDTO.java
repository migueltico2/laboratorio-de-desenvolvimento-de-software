package com.example.project_2.model.DTO;

public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String user_token;
    private String password;
    // Construtores, getters e setters

    public UserDTO(Long id, String name, String email, String user_token) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.user_token = user_token;
    }

    public UserDTO() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserToken() {
        return this.user_token;
    }

    public void setUserToken(String user_token) {
        this.user_token = user_token;
    }

}
