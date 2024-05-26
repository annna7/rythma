package commands.concrete.artists.albums;

import commands.Command;
import models.audio.collections.Album;
import models.users.Artist;
import models.users.User;
import search.strategies.AlbumStrategy;
import services.MusicService;

import java.util.List;
import java.util.function.Predicate;

import static utils.OutputUtils.showCollectionMessage;

public class ViewAlbumsCommand implements Command {
    @Override
    public void execute() {
        List<Album> albumList = MusicService.getInstance().getCurrentAlbums();
        showCollectionMessage("albums", albumList);
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
