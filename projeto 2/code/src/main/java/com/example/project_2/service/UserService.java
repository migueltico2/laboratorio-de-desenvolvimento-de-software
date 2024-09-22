package com.example.project_2.service;

import com.example.project_2.model.User;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private List<User> users;
    private static final String DATA_FILE = "users.dat";

    public UserService() {
        this.users = loadUsers();
    }

    @SuppressWarnings("unchecked")
    private List<User> loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            return (List<User>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public Optional<User> getUserById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    public User createUser(String name, String email, String password) {
        User newUser = new User(name, email, password, null);
        users.add(newUser);
        saveUsers();
        return newUser;
    }

    public Optional<User> updateUser(Long id, String name, String email) {
        Optional<User> userOpt = getUserById(id);
        userOpt.ifPresent(user -> {
            user.setName(name);
            user.setEmail(email);
            saveUsers();
        });
        return userOpt;
    }

    public boolean deleteUser(Long id) {
        boolean removed = users.removeIf(user -> user.getId().equals(id));
        if (removed) {
            saveUsers();
        }
        return removed;
    }

    public boolean login(Long id, String password) {
        return getUserById(id)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }

    public boolean changePassword(Long id, String oldPass, String newPass) {
        return getUserById(id).map(user -> {
            if (user.getPassword().equals(oldPass)) {
                user.setPassword(newPass);
                saveUsers();
                return true;
            }
            return false;
        }).orElse(false);
    }
}
