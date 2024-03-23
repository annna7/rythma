package commands.concrete.artists;

import commands.Command;
import models.audio.collections.Album;
import services.MusicService;

import java.util.ArrayList;

public class ViewAlbumsCommand implements Command {
    @Override
    public void execute() {
        ArrayList<Album> albums = MusicService.getInstance().getCurrentAlbums();
        if (albums.isEmpty()) {
            System.out.println("No albums found.");
        } else {
            System.out.println("Albums:");
            for (Album album : albums) {
                System.out.println(album);
            }
        }
    }
}
