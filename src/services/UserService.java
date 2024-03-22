package services;

import models.users.User;

public class UserService {
    private static UserService instance = null;
    private UserService() {
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public void login() {}
    public void register() {}

    public User getCurrentUser() {
        return null;
    }
}
