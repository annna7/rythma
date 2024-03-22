package services;

import commands.Command;
import commands.concrete.CreatePlaylistCommand;
import commands.concrete.ExitCommand;
import commands.concrete.LoginCommand;
import commands.concrete.RegisterCommand;

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

    static {
        loggedInCommands.put(1, new SimpleEntry<>(new CreatePlaylistCommand(), "Create Playlist"));
        loggedInCommands.put(2, new SimpleEntry<>(new ExitCommand(), "Exit"));

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
        Map<Integer, SimpleEntry<Command, String>> currentCommands = userService.getCurrentUser() == null ? loggedOutCommands : loggedInCommands;

        System.out.println("Welcome to Rythma, your Music Streaming Application!");
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
