package services;

import enums.UserRoleEnum;
import exceptions.NotFoundException;
import models.users.Artist;
import models.users.Host;
import models.users.User;
import utils.RoleValidator;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private User currentUser = null;
    private static UserService instance = null;
    private final static List<User> users = new ArrayList<>();

    private UserService() {}

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean login(String username, String password) {
        User user = getUserByUsername(username);
        if (user.getPassword().equals(password)) {
            setCurrentUser(user);
            return true;
        } else {
            return false;
        }
    }

    public void register(User user) {
        users.add(user);
        setCurrentUser(user);
    }

    private void setCurrentUser(User user) {
        currentUser = user;
    }

    public void logout() {
        setCurrentUser(null);
    }

    public User getUserById(int userId) {
        return users.stream().filter(u -> u.getId() == userId).findFirst().orElseThrow(() -> new NotFoundException("User"));
    }

    public User getUserByUsername(String username) {
        return users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElseThrow(() -> new NotFoundException("User"));
    }

    public Artist getArtistById(int artistId) {
        return RoleValidator.validateRole(getUserById(artistId), UserRoleEnum.ARTIST, Artist.class);
    }

    public Host getHostById(int hostId) {
        return RoleValidator.validateRole(getUserById(hostId), UserRoleEnum.HOST, Host.class);
    }

    public User getCurrentUser() {
        return getCurrentUser(false);
    }

    public User getCurrentUser(boolean allowNull) {
        if (allowNull) {
            return currentUser;
        } else {
            return RoleValidator.validateRole(currentUser, UserRoleEnum.REGULAR, User.class);
        }
    }

    public Artist getCurrentArtist() {
        return RoleValidator.validateRole(currentUser, UserRoleEnum.ARTIST, Artist.class);
    }

    public Host getCurrentHost() {
        return RoleValidator.validateRole(currentUser, UserRoleEnum.HOST, Host.class);
    }

    public void updateHostAffiliation(String affiliation) {
        getCurrentHost().setAffiliation(affiliation);
    }

    public void addSocialMediaLinkToArtist(String platform, String link) {
        getCurrentArtist().addSocialMediaLink(platform, link);
    }

    public void removeSocialMediaLinkFromArtist(String platform) {
        getCurrentArtist().removeSocialMediaLink(platform);
    }

    public List<Artist> getAllArtists() {
        return users.stream().filter(u -> u instanceof Artist).map(u -> (Artist) u).toList();
    }

    public List<Host> getAllHosts() {
        return users.stream().filter(u -> u instanceof Host).map(u -> (Host) u).toList();
    }
}
