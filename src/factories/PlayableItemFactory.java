package factories;

import enums.PlayableItemEnum;
import models.audio.items.Episode;
import models.audio.items.PlayableItem;
import models.audio.items.Song;

import java.time.LocalDate;
import java.util.List;

public class PlayableItemFactory {
    public static PlayableItem createPlayableItem(PlayableItemEnum type, String title, int length, LocalDate release, int albumOrPodcastId, List<String> additionalInfo) {
        return switch (type) {
            case SONG -> {
                Song song = new Song(title, length, albumOrPodcastId, release);
                for (String info : additionalInfo) {
                    song.addGenre(info);
                }
                yield song;
            }
            case EPISODE -> {
                Episode episode = new Episode(title, length, albumOrPodcastId, release);
                for (String info : additionalInfo) {
                    episode.addGuest(info);
                }
                yield episode;
            }
        };
    }
}
