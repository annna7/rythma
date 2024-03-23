package commands.concrete.users.playlists;

import commands.Command;
import enums.AudioCollectionEnum;
import factories.AudioCollectionFactory;
import models.audio.collections.Playlist;
import services.PlaylistService;

import static utils.InputUtils.askForField;

public class AddPlaylistCommand implements Command {
    @Override
    public void execute() {
        String playlistName = askForField("playlist name");
        String playlistDescription = askForField("playlist description");

        Playlist playlist = (Playlist) AudioCollectionFactory.createAudioCollection(AudioCollectionEnum.PLAYLIST, playlistName, playlistDescription);

        PlaylistService.getInstance().addPlaylist(playlist);
    }
}
