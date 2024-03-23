package services;

import models.users.Artist;
import models.users.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserService {
    private final HashMap<String, User> usersByUsername = new HashMap<>();
    private User currentUser = null;
    private static UserService instance = null;
    private UserService() {}

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public boolean login(String username, String password) {
        if (usersByUsername.containsKey(username)) {
            User user = usersByUsername.get(username);
            if (user.getPassword().equals(password)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    public void register(User user) {
        usersByUsername.put(user.getUsername(), user);
        currentUser = user;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
