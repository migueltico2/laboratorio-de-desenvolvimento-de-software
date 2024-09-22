package com.example.project_2.service;

import com.example.project_2.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final List<User> users = new ArrayList<>();

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public Optional<User> getUserById(int id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst();
    }

    public User createUser(String name, String email, String password) {
        User newUser = new User(name, email, password, null);
        users.add(newUser);
        return newUser;
    }

    public Optional<User> updateUser(int id, String name, String email) {
        return getUserById(id).map(user -> {
            user.setName(name);
            user.setEmail(email);
            return user;
        });
    }

    public boolean deleteUser(int id) {
        return users.removeIf(user -> user.getId() == id);
    }

    public boolean login(int id, String password) {
        return getUserById(id)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }

    public boolean changePassword(int id, String oldPass, String newPass) {
        return getUserById(id).map(user -> {
            if (user.getPassword().equals(oldPass)) {
                user.setPassword(newPass);
                return true;
            }
            return false;
        }).orElse(false);
    }
}
