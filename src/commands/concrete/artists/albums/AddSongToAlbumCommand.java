package commands.concrete.artists.albums;

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
        String title = askForField("song title");
        int length = Integer.parseInt(askForField("song length"));
        int albumId = Integer.parseInt(askForField("album ID"));

        LocalDate release = askForReleaseDate();
        ArrayList< String > genres = askForGenres();

        Song song = (Song) PlayableItemFactory.createPlayableItem(PlayableItemEnum.SONG, title, length, release, albumId,
                                                                  genres.toArray(new String[0]));

        MusicService.getInstance().addSongToAlbum(song, albumId);
    }
}
