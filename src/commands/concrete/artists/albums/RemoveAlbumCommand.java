package commands.concrete.artists.albums;

import commands.Command;
import models.users.Artist;
import models.users.User;
import services.MusicService;

import java.util.function.Predicate;

import static utils.InputUtils.askForField;
public class RemoveAlbumCommand implements Command {
    @Override
    public void execute() {
        int albumId = Integer.parseInt(askForField("the album ID"));
        MusicService.getInstance().removeAlbum(albumId);
    }

    @Override
    public Predicate<User> getVisibilityRule() {
        return user -> user instanceof Artist;
    }

    @Override
    public String getCommandName() {
        return "Remove album";
    }

    @Override
    public String getCommandDescription() {
        return "Remove one of your albums, by providing the album ID";
    }
}
