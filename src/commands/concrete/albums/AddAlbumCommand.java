package commands.concrete.albums;

import commands.Command;
import enums.AudioCollectionEnum;
import factories.AudioCollectionFactory;
import models.audio.collections.Album;
import services.MusicService;

import static utils.InputUtils.askForField;

public class AddAlbumCommand implements Command {
    @Override
    public void execute() {
        String name = askForField("the album name");
        String label = askForField("the album label");

        Album album = (Album) AudioCollectionFactory.createAudioCollection(AudioCollectionEnum.ALBUM, name, label);
        MusicService.getInstance().addAlbum(album);
        // TODO: Audit & database
    }
}
