package factories;

import enums.AudioCollectionEnum;
import models.audio.collections.Album;
import models.audio.collections.AudioCollection;

public class AudioCollectionFactory {
    public static AudioCollection createAudioCollection(AudioCollectionEnum type, String name, String... args) {
        return switch (type) {
//            case PLAYLIST -> new Playlist(name);
            case ALBUM -> new Album(name, args[0]);
//            case PODCAST -> new Podcast(name, args[0]);
            default -> throw new IllegalArgumentException("Invalid audio collection type");
        };
    }
}
