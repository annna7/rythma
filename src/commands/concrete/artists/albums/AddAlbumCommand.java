package commands.concrete.artists.albums;

import commands.Command;
import enums.AudioCollectionEnum;
import factories.AudioCollectionFactory;
import models.audio.collections.Album;
import models.users.Artist;
import models.users.User;
import services.MusicService;

import java.util.function.Predicate;

import static utils.InputUtils.askForField;

public class AddAlbumCommand implements Command {
    @Override
    public void execute() {
        String name = askForField("the album name");
        String label = askForField("the album label");

        Album album = (Album) AudioCollectionFactory.createAudioCollection(AudioCollectionEnum.ALBUM, name, label);
        MusicService.getInstance().addAlbum(album);
        System.out.println("Album added successfully.");
    }

    @Override
    public Predicate<User> getVisibilityRule() {
        return user -> user instanceof Artist;
    }

    @Override
    public String getCommandName() {
        return "Add album";
    }

    @Override
    public String getCommandDescription() {
        return "Create a new album, which can later be populated with tracks";
    }
}
