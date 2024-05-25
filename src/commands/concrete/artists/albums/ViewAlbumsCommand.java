package commands.concrete.artists.albums;

import commands.Command;
import models.users.Artist;
import models.users.User;
import services.MusicService;

import java.sql.SQLException;
import java.util.function.Predicate;

import static utils.OutputUtils.showCollectionMessage;

public class ViewAlbumsCommand implements Command {
    @Override
    public void execute() throws SQLException {
        showCollectionMessage("albums", MusicService.getInstance().getCurrentAlbums());
    }

    @Override
    public Predicate<User> getVisibilityRule() {
        return user -> user instanceof Artist;
    }

    @Override
    public String getCommandName() {
        return "View albums";
    }

    @Override
    public String getCommandDescription() {
        return "View all albums for the currently logged in artist";
    }
}
