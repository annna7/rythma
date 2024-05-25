package commands.concrete.artists.albums;

import commands.Command;
import enums.PlayableItemEnum;
import factories.PlayableItemFactory;
import models.audio.items.Song;
import models.users.Artist;
import models.users.User;
import services.MusicService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Predicate;

import static utils.InputUtils.*;

public class AddSongToAlbumCommand implements Command {
    @Override
    public void execute() throws SQLException {
        String title = askForField("song title");
        int length = Integer.parseInt(askForField("song length"));
        int albumId = Integer.parseInt(askForField("album ID"));

        LocalDate release = askForReleaseDate();
        ArrayList< String > genres = askForGenres();

        Song song = (Song) PlayableItemFactory.createPlayableItem(PlayableItemEnum.SONG, title, length, release, albumId,
                                                                  genres.toArray(new String[0]));

        MusicService.getInstance().addSongToAlbum(song, albumId);
    }

    @Override
    public Predicate<User> getVisibilityRule() {
        return user -> user instanceof Artist;
    }

    @Override
    public String getCommandName() {
        return "Add song to album";
    }

    @Override
    public String getCommandDescription() {
        return "Create a new song and add it to an album, by providing the song title, length, album ID, release date, and genres";
    }
}
