package services;

import enums.UserRoleEnum;
import exceptions.BadLoginAttemptException;
import exceptions.CurrentUserNotInDatabaseException;
import exceptions.ExistingUsernameException;
import exceptions.NotFoundException;
import models.users.Artist;
import models.users.Host;
import models.users.User;
import repositories.users.ArtistRepository;
import repositories.users.HostRepository;
import repositories.users.UserRepository;
import utils.RoleValidator;

import java.sql.SQLException;
import java.util.*;

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

    public void login(String username, String password, UserRoleEnum role) throws BadLoginAttemptException {
        try {
            User user = this.getUserByUsername(username);
            if (user.getPassword().equals(password)) {
                switch (role) {
                    case ARTIST:
                        setCurrentUser(getArtistById(user.getId()));
                        break;
                    case HOST:
                        setCurrentUser(getHostById(user.getId()));
                        break;
                    default:
                        setCurrentUser(user);
                }
            } else {
                throw new BadLoginAttemptException();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new BadLoginAttemptException();
        }
    }

    public void register(User user, UserRoleEnum role) {
        try {
            User existingUser = this.getUserByUsername(user.getUsername());
            throw new ExistingUsernameException(existingUser.getUsername());
        } catch (NotFoundException e) {
            // if a user with this username doesn't already exist, then the operation is successful
            // and create it in the database
            users.add(user);
            try {
                switch (role) {
                    case ARTIST:
                        artistRepository.create((Artist) user);
                        break;
                    case HOST:
                        hostRepository.create((Host) user);
                        break;
                    default:
                        userRepository.create(user);
                }
            } catch (SQLException sqlException) {
                System.out.printf("SQL error: %s%n", sqlException.getMessage());
            }
            setCurrentUser(user);
        }
    }

    private void setCurrentUser(User user) {
        currentUser = user;
    }

    public void logout() {
        setCurrentUser(null);
    }

    public User getUserById(int userId) throws NotFoundException {
        // try to find user in cache
        Optional<User> cachedUser = users.stream()
                .filter(u -> u.getId() == userId)
                .findFirst();

        if (cachedUser.isPresent()) {
            return cachedUser.get();
        }

        // otherwise, try to find in the database
        try {
            Optional<User> user = userRepository.findById(userId);
            if (user.isPresent()) {
                users.add(user.get());
                return user.get();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // otherwise, not found
        throw new NotFoundException(String.format("User with id %d", userId));
    }

    public User getUserByUsername(String username) throws NotFoundException {
        Optional<User> cachedUser = users.stream()
                .filter(u -> Objects.equals(u.getUsername(), username))
                .findFirst();

        if (cachedUser.isPresent()) {
            return cachedUser.get();
        }

        try {
            Optional<User> user = userRepository.findByUsername(username);
            if (user.isPresent()) {
                users.add(user.get());
                return user.get();
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }

        throw new NotFoundException("User with username " + username + " not found");
    }


    public Artist getArtistById(int artistId) throws NotFoundException {
        try {
            Optional<Artist> artistOptional = users.stream()
                    .filter(u -> u.getId() == artistId && u instanceof Artist)
                    .map(u -> (Artist) u)
                    .findFirst();

            if (artistOptional.isEmpty()) {
                artistOptional = artistRepository.findById(artistId);
                artistOptional.ifPresent(users::add);
            }

            if (artistOptional.isPresent()) {
                return artistOptional.get();
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        throw new NotFoundException("Artist with id " + artistId + " not found due to SQL error");
    }

    public Host getHostById(int hostId) throws NotFoundException {
        try {
            Optional<Host> hostOptional = users.stream()
                    .filter(u -> u.getId() == hostId && u instanceof Host)
                    .map(u -> (Host) u)
                    .findFirst();

            if (hostOptional.isEmpty()) {
                hostOptional = hostRepository.findById(hostId);
                hostOptional.ifPresent(users::add);
            }

            if (hostOptional.isPresent()) {
                return hostOptional.get();
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        throw new NotFoundException("Host with id " + hostId + " not found");
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
        try {
            Host host = getCurrentHost();
            if (!hostRepository.update(host)) {
                throw new CurrentUserNotInDatabaseException(host.getUsername());
            }
            host.setAffiliation(affiliation);
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }

    public void addSocialMediaLinkToArtist(String platform, String link) {
        try {
            Artist artist = getCurrentArtist();
            if (!artistRepository.update(artist)) {
                throw new CurrentUserNotInDatabaseException(artist.getUsername());
            }
            artist.addSocialMediaLink(platform, link);
        }
        catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }

    public void removeSocialMediaLinkFromArtist(String platform) {
        try {
            Artist artist = getCurrentArtist();
            if (!artistRepository.update(artist)) {
                throw new CurrentUserNotInDatabaseException(artist.getUsername());
            }
            artist.removeSocialMediaLink(platform);
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }

    public List<Artist> getAllArtists() {
        try {
            return artistRepository.findAll();
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            return List.of();
        }
    }

    public List<Host> getAllHosts() {
        try {
            return hostRepository.findAll();
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            return List.of();
        }
    }
}
