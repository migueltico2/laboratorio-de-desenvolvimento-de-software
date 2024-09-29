package com.example.project_2.controller;

import com.example.project_2.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BaseController {
    protected static List<User> users;
    private static final String USERS_FILE = "users.dat";

    static {
        users = loadUsers(USERS_FILE);
    }

    @SuppressWarnings("unchecked")
    protected static List<User> loadUsers(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (List<User>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    protected static void saveUsers(String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}