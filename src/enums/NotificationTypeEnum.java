package enums;

public enum NotificationTypeEnum {
    NEW_SONG_RELEASE,
    NEW_EPISODE,
    NEW_SONG_ADDED_TO_PLAYLIST;

    public int getPriority() {
        return switch (this) {
            case NEW_SONG_RELEASE -> 3;
            case NEW_EPISODE -> 2;
            case NEW_SONG_ADDED_TO_PLAYLIST -> 1;
        };
    }
}
