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
        switch (this) {
            case SONG:
                return new SongStrategy();
            case ALBUM:
                return new AlbumStrategy();
            case ARTIST:
                return new ArtistStrategy();
            case PODCAST:
                return new PodcastStrategy();
            case PLAYLIST:
                return new PlaylistStrategy();
            case HOST:
                return new PodcastHostStrategy();
            case PODCAST_EPISODE:
                return new PodcastEpisodeStrategy();
            default:
                return null;
        }
    }
}