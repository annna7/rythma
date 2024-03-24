package services;

import commands.Command;
import commands.concrete.artists.*;
import commands.concrete.artists.albums.*;
import commands.concrete.hosts.UpdateAffiliationCommand;
import commands.concrete.hosts.podcasts.*;
import commands.concrete.users.*;
import commands.concrete.users.notifications.*;
import commands.concrete.users.playlists.*;

import java.util.*;


public class CliService {
    private final UserService userService;
    private static final List<Command> commands = new ArrayList<>();
    static {
        commands.add(new ShowUserDetailsCommand());
        commands.add(new ViewPlaylistsCommand());
        commands.add(new AddPlaylistCommand());
        commands.add(new AddSongToPlaylistCommand());
        commands.add(new RemoveSongFromPlaylistCommand());
        commands.add(new RemovePlaylistCommand());
        commands.add(new ViewAlbumsCommand());
        commands.add(new AddAlbumCommand());
        commands.add(new AddSongToAlbumCommand());
        commands.add(new RemoveSongFromAlbumCommand());
        commands.add(new RemoveAlbumCommand());
        commands.add(new AddSocialMediaLinkCommand());
        commands.add(new RemoveSocialMediaLinkCommand());
        commands.add(new ViewPodcastsCommand());
        commands.add(new AddPodcastCommand());
        commands.add(new AddEpisodeToPodcastCommand());
        commands.add(new RemoveEpisodeFromPodcastCommand());
        commands.add(new RemovePodcastCommand());
        commands.add(new UpdateAffiliationCommand());
        commands.add(new FollowUnfollowArtistCommand());
        commands.add(new FollowUnfollowHostCommand());
        commands.add(new FollowUnfollowPlaylistCommand());
        commands.add(new ViewNotificationsCommand());
        commands.add(new ClearNotificationsCommand());
        commands.add(new TogglePlaylistVisibilityCommand());
        commands.add(new SearchCommand());
        commands.add(new LoginCommand());
        commands.add(new RegisterCommand());
        commands.add(new ExitCommand());
    }

    private static CliService instance = null;
    private CliService() {
        userService = UserService.getInstance();
    }

    public static synchronized CliService getInstance() {
        if (instance == null) {
            instance = new CliService();
        }
        return instance;
    }

    private List<Command> getAvailableCommands() {
        return commands.stream().filter(command -> command.getVisibilityRule().test(UserService.getInstance().getCurrentUser(true))).toList();
    }

    private Command selectCommand(int option) {
        if (option < 1 || option > getAvailableCommands().size()) {
            return null;
        }
        return getAvailableCommands().get(option - 1);
    }

    public void showMainMenu() {
        if (UserService.getInstance().isLoggedIn()) {
            System.out.println("Welcome, " + userService.getCurrentUser().getUsername() + "!");
        } else {
            System.out.println("Welcome to Rythma, your Music Streaming Application!");
        }
        System.out.println("Select an option:");
        List<Command> availableCommands = getAvailableCommands();
        for (int i = 0; i < availableCommands.size(); i++) {
            System.out.println((i + 1) + ". " + availableCommands.get(i).getCommandName() + " - " + availableCommands.get(i).getCommandDescription());
        }
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            showMainMenu();
            int option = scanner.nextInt();
            Command selectedCommand = selectCommand(option);
            if (selectedCommand == null) {
                System.out.println("Invalid option. Please try again.");
            } else {
                selectedCommand.execute();
                if (selectedCommand instanceof ExitCommand) {
                    System.out.println("Thank you for using Rythma!");
                    break;
                }
            }
        }
    }
}
