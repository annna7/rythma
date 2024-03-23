package commands.concrete.artists;

import commands.Command;
import enums.PlayableItemEnum;
import factories.PlayableItemFactory;
import models.audio.items.Song;
import services.MusicService;

import java.time.LocalDate;
import java.util.ArrayList;

import static utils.InputUtils.*;

public class AddSongToAlbumCommand implements Command {
    @Override
    public void execute() {
        String title = askForField("the song title");
        int length = Integer.parseInt(askForField("the song length"));
        int albumId = Integer.parseInt(askForField("the album ID"));

        LocalDate release = askForReleaseDate();
        ArrayList< String > genres = askForGenres();

        Song song = (Song) PlayableItemFactory.createPlayableItem(PlayableItemEnum.SONG, title, length, release, albumId, genres);
        try {
            MusicService.getInstance().addSongToAlbum(song, albumId);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
