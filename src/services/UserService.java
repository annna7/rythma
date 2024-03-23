package services;

import enums.UserRoleEnum;
import models.users.Artist;
import models.users.Host;
import models.users.User;
import java.util.HashMap;

public class UserService {
    private final HashMap<String, User> usersByUsername = new HashMap<>();
    private User currentUser = null;
    private UserRoleEnum role = null;
    private static UserService instance = null;

    private UserService() {}

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    private boolean setRole() {
        if (currentUser instanceof models.users.Artist) {
            role = UserRoleEnum.ARTIST;
            return true;
        } else if (currentUser instanceof models.users.Host) {
            role = UserRoleEnum.HOST;
            return true;
        } else {
            role = UserRoleEnum.REGULAR;
            return true;
        }
    }

    public boolean login(String username, String password) {
        if (usersByUsername.containsKey(username)) {
            User user = usersByUsername.get(username);
            if (user.getPassword().equals(password)) {
                currentUser = user;
                setRole();
                return true;
            }
        }
        return false;
    }

    public void register(User user) {
        usersByUsername.put(user.getUsername(), user);
        currentUser = user;
        setRole();
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void updateHostAffiliation(String affiliation) {
        Host currentUser = (Host) UserService.getInstance().getCurrentUser();
        if (currentUser == null) {
            throw new IllegalArgumentException("Current user is not a host");
        }
        currentUser.setAffiliation(affiliation);
    }

    public void addSocialMediaLinkToArtist(String platform, String link) {
        Artist currentUser = (Artist) UserService.getInstance().getCurrentUser();
        if (currentUser == null) {
            throw new IllegalArgumentException("Current user is not an artist");
        }
        currentUser.addSocialMediaLink(platform, link);
    }

    public void removeSocialMediaLinkFromArtist(String platform) {
        Artist currentUser = (Artist) UserService.getInstance().getCurrentUser();
        if (currentUser == null) {
            throw new IllegalArgumentException("Current user is not an artist");
        }
        currentUser.removeSocialMediaLink(platform);
    }

    public UserRoleEnum getRole() {
        return role;
    }
}
