package enums;

import search.SearchStrategy;
import search.strategies.*;

public enum SearchStrategyEnum {
    SONG,
    ALBUM,
    ARTIST,
    PODCAST,
    PLAYLIST,
    HOST,
    PODCAST_EPISODE;

    public SearchStrategy getStrategyFromEnum() {
        return switch (this) {
            case SONG -> new SongStrategy();
            case ALBUM -> new AlbumStrategy();
            case ARTIST -> new ArtistStrategy();
            case PODCAST -> new PodcastStrategy();
            case PLAYLIST -> new PlaylistStrategy();
            case HOST -> new PodcastHostStrategy();
            case PODCAST_EPISODE -> new PodcastEpisodeStrategy();
            default -> null;
        };
    }
}