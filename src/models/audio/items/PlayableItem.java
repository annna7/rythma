package models.audio.items;

import java.time.LocalDate;

public abstract class PlayableItem {
    protected final String title;
    protected final int length;
    protected final LocalDate release;

    public PlayableItem(String title, int length, LocalDate release) {
        this.title = title;
        this.length = length;
        this.release = release;
    }

    public String getTitle() {
        return title;
    }

    public int getLength() {
        return length;
    }
}
