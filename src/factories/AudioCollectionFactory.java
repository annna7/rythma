package factories;

import enums.AudioCollectionEnum;
import models.audio.collections.*;

public class AudioCollectionFactory {
    // TODO: Can we fix this?
    public static AudioCollection createAudioCollection(AudioCollectionEnum type, String name, String... additionalInfo) {
        return switch (type) {
            case PLAYLIST -> new Playlist(name, additionalInfo[0], additionalInfo[1]);
            case ALBUM -> new Album(name, additionalInfo[0]);
            case PODCAST -> new Podcast(name, additionalInfo[0]);
        };
    }
}
