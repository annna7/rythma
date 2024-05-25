package commands.concrete.artists.albums;

import commands.Command;
import models.users.Artist;
import models.users.User;
import services.MusicService;

import java.sql.SQLException;
import java.util.function.Predicate;

import static utils.InputUtils.askForField;

public class RemoveSongFromAlbumCommand implements Command {
    @Override
    public void execute() throws SQLException {
        int songId = Integer.parseInt(askForField("the song ID"));

        MusicService.getInstance().removeSongFromAlbum(songId);
    }

    @Override
    public Predicate<User> getVisibilityRule() {
        return user -> user instanceof Artist;
    }

    @Override
    public String getCommandName() {
        return "Remove song from album";
    }

    @Override
    public String getCommandDescription() {
        return "Remove a song from one of your albums, by providing the song ID";
    }
}
