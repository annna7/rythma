package models.audio.items;

import java.time.LocalDate;

public abstract class PlayableItem {
    private static int idCounter = 0;
    private final int id = idCounter++;
    protected final String title;
    protected final int length;
    protected final LocalDate release;

    public PlayableItem(String title, int length, LocalDate release) {
        this.title = title;
        this.length = length;
        this.release = release;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getLength() {
        return length;
    }

    @Override
    public String toString() {
        return "PlayableItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", length=" + length +
                ", release=" + release +
                '}';
    }
}
