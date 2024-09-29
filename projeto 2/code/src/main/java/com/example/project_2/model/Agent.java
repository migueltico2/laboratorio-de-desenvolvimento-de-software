package com.example.project_2.model;

public class Agent extends User {

    public Agent(String name, String email, String password, String role) {
        super(null, name, email, password, null, role);
    }

    public Agent(Long id, String name, String email, String password, String role) {
        super(id, name, email, password, null, role);
    }

    public Agent() {
        super();
    }

}
