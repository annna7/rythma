package services;

import enums.UserRoleEnum;
import exceptions.BadLoginAttemptException;
import exceptions.CurrentUserNotInDatabaseException;
import exceptions.ExistingUsernameException;
import exceptions.NotFoundException;
import models.users.Artist;
import models.users.Host;
import models.users.User;
import repositories.ArtistRepository;
import repositories.HostRepository;
import repositories.UserRepository;
import utils.RoleValidator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserService {
    private static UserService instance = null;
    private User currentUser = null;
    private final List<User> users = new ArrayList<>();

    private final UserRepository userRepository = new UserRepository();
    private final ArtistRepository artistRepository = new ArtistRepository();
    private final HostRepository hostRepository = new HostRepository();

    private UserService() {}

    public static synchronized UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public void login(String username, String password) throws BadLoginAttemptException {
        try {
            User user = this.getUserByUsername(username);
            if (user.getPassword().equals(password)) {
                setCurrentUser(user);
                System.out.println("good login");
            } else {
                throw new BadLoginAttemptException();
            }
        } catch (Exception e) {
            throw new BadLoginAttemptException();
        }
    }

    public void register(User user) {
        try {
            User existingUser = this.getUserByUsername(user.getUsername());
            throw new ExistingUsernameException(existingUser.getUsername());
        } catch (NotFoundException e) {
            // if a user with this username doesn't already exist, then the operation is successful
            // and create it in the database
            users.add(user);
            userRepository.create(user);
            setCurrentUser(user);
        }
    }

    private void setCurrentUser(User user) {
        currentUser = user;
    }

    public void logout() {
        setCurrentUser(null);
    }

    public User getUserById(int userId) {
        return users.stream()
                .filter(u -> u.getId() == userId)
                .findFirst()
                .orElseGet(() -> {
                    Optional<User> user = userRepository.findById(userId);
                    if (user.isPresent()) {
                        users.add(user.get());
                        return user.get();
                    }
                    throw new NotFoundException(String.format("User with id %d not found", userId));
                });
    }

    public User getUserByUsername(String username) {
        return users.stream()
                .filter(u -> Objects.equals(u.getUsername(), username))
                .findFirst()
                .orElseGet(() -> {
                    Optional<User> user = userRepository.findByUsername(username);
                    if (user.isPresent()) {
                        users.add(user.get());
                        return user.get();
                    }
                    throw new NotFoundException(String.format("User with name %s not found", username));
                });
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

    public void updateHostAffiliation(String affiliation) throws SQLException {
        Host host = getCurrentHost();
        if (!hostRepository.update(host)) {
            throw new CurrentUserNotInDatabaseException(host.getUsername());
        }
        host.setAffiliation(affiliation);
    }

    public void addSocialMediaLinkToArtist(String platform, String link) throws SQLException {
        Artist artist = getCurrentArtist();
        if (!artistRepository.update(artist)) {
            throw new CurrentUserNotInDatabaseException(artist.getUsername());
        }
        artist.addSocialMediaLink(platform, link);
    }

    public void removeSocialMediaLinkFromArtist(String platform) throws SQLException {
        Artist artist = getCurrentArtist();
        if (!artistRepository.update(artist)) {
            throw new CurrentUserNotInDatabaseException(artist.getUsername());
        }
        artist.removeSocialMediaLink(platform);
    }

    public List<Artist> getAllArtists() throws SQLException {
        return artistRepository.findAll();
    }

    public List<Host> getAllHosts() throws SQLException {
        return hostRepository.findAll();
    }
}
