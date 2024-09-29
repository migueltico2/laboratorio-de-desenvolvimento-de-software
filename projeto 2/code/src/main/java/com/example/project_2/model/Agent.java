package com.example.project_2.model;

public class Agent extends User {

    public Agent(String name, String email, String password) {
        super(null, name, email, password, null);
    }

    public Agent(Long id, String name, String email, String password) {
        super(id, name, email, password, null);
    }

    public Agent() {
        super();
    }

}
