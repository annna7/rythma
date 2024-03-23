package services;

import commands.Command;
import commands.concrete.albums.*;
import commands.concrete.users.*;
import commands.concrete.playlists.*;
import enums.UserRoleEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.AbstractMap.SimpleEntry;


public class CliService {
    private final UserService userService;
    private final MusicService musicService;
    private final PlaylistService playlistService;
    private final MusicPlayerService musicPlayerService;
    private static final Map<Integer, SimpleEntry<Command, String>> loggedInCommands = new HashMap<>();
    private static final Map<Integer, SimpleEntry<Command, String>> loggedOutCommands = new HashMap<>();
    private static final Map<Integer, SimpleEntry<Command, String>> artistCommands = new HashMap<>();
    private static final Map<Integer, SimpleEntry<Command, String>> hostCommands = new HashMap<>();

    static {
        loggedInCommands.put(1, new SimpleEntry<>(new CreatePlaylistCommand(), "Create Playlist"));

        artistCommands.put(1, new SimpleEntry<>(new ViewAlbumsCommand(), "View All Your Albums"));
        artistCommands.put(2, new SimpleEntry<>(new AddAlbumCommand(), "Create Album"));
        artistCommands.put(3, new SimpleEntry<>(new AddSongToAlbumCommand(), "Add Song to Album"));
        artistCommands.put(4, new SimpleEntry<>(new RemoveSongFromAlbumCommand(), "Remove Song from Album"));
        artistCommands.put(5, new SimpleEntry<>(new RemoveAlbumCommand(), "Remove Album"));

        loggedOutCommands.put(1, new SimpleEntry<>(new LoginCommand(), "Login"));
        loggedOutCommands.put(2, new SimpleEntry<>(new RegisterCommand(), "Register"));
        loggedOutCommands.put(3, new SimpleEntry<>(new ExitCommand(), "Exit"));
    }

    private static CliService instance = null;
    private CliService() {
        userService = UserService.getInstance();
        musicService = MusicService.getInstance();
        playlistService = PlaylistService.getInstance();
        musicPlayerService = MusicPlayerService.getInstance();
    }

    public static CliService getInstance() {
        if (instance == null) {
            instance = new CliService();
        }
        return instance;
    }

    public void showMainMenu() {
        boolean isLoggedIn = userService.getCurrentUser() != null;
        Map<Integer, SimpleEntry<Command, String>> currentCommands = isLoggedIn ? loggedOutCommands : loggedInCommands;
        if (userService.getRole() == UserRoleEnum.ARTIST) {
            currentCommands.putAll(artistCommands);
        } else if (userService.getRole() == UserRoleEnum.HOST) {
            currentCommands.putAll(hostCommands);
        }

        if (isLoggedIn) {
            System.out.println("Welcome, " + userService.getCurrentUser().getUsername() + "!");
        } else {
            System.out.println("Welcome to Rythma, your Music Streaming Application!");
        }

        System.out.println("Select an option:");
        currentCommands.forEach((key, value) -> System.out.println(key + ". " + value.getValue()));
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            showMainMenu();
            int option = scanner.nextInt();
            Map<Integer, SimpleEntry<Command, String>> currentCommands = userService.getCurrentUser() == null ? loggedOutCommands : loggedInCommands;

            SimpleEntry<Command, String> selectedCommandPair = currentCommands.get(option);
            if (selectedCommandPair != null) {
                selectedCommandPair.getKey().execute();
                if (selectedCommandPair.getKey() instanceof ExitCommand) {
                    System.out.println("Thank you for using Rythma!");
                    break;
                }
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
