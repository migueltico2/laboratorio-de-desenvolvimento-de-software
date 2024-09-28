package com.example.project_2.controller;

import com.example.project_2.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BaseController {
    protected static List<User> users;
    private static final String DATA_FILE = "users.dat";

    static {
        users = loadUsers();
    }

    @SuppressWarnings("unchecked")
    protected static List<User> loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            return (List<User>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    protected static void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}