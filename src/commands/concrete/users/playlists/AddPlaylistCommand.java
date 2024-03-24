package commands.concrete.users.playlists;

import commands.Command;
import enums.AudioCollectionEnum;
import factories.AudioCollectionFactory;
import models.audio.collections.Playlist;
import models.users.User;
import services.PlaylistService;

import java.util.Objects;
import java.util.function.Predicate;

import static utils.InputUtils.askForField;

public class AddPlaylistCommand implements Command {
    @Override
    public void execute() {
        String playlistName = askForField("playlist name");
        String playlistDescription = askForField("playlist description");
        String isPublic = askForField("playlist visibility (true/false for public/private)");

        Playlist playlist = (Playlist) AudioCollectionFactory.createAudioCollection(AudioCollectionEnum.PLAYLIST, playlistName, playlistDescription, isPublic);

        PlaylistService.getInstance().addPlaylist(playlist);
    }

    @Override
    public Predicate<User> getVisibilityRule() {
        return Objects::nonNull;
    }

    @Override
    public String getCommandName() {
        return "Add playlist";
    }

    @Override
    public String getCommandDescription() {
        return "Create a new playlist, which can later be populated with songs";
    }
}
