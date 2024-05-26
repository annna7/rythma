package factories;

import enums.PlayableItemEnum;
import models.audio.items.Episode;
import models.audio.items.PlayableItem;
import models.audio.items.Song;

import java.time.LocalDate;

public class PlayableItemFactory {
    public static PlayableItem createPlayableItem(PlayableItemEnum type, String title, int length, LocalDate release,
                                                  int collectionId, String... additionalInfo) {
        return switch (type) {
            case SONG -> {
                Song song = new Song(title, length, collectionId, release);
                for (String info : additionalInfo) {
                    song.addGenre(info);
                }
                yield song;
            }
            case EPISODE -> {
                // args[0] - description
                // args[1] - number
                // rest - guests
                Episode episode = new Episode(title, length, release, collectionId, Integer.parseInt(additionalInfo[1]),
                                              additionalInfo[0]);
                for (int i = 2; i < additionalInfo.length; i++) {
                    episode.addGuest(additionalInfo[i]);
                }
                yield episode;
            }
        };
    }
}
