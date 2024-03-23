package models.audio.items;

import java.time.LocalDate;

public abstract class PlayableItem {
    private static int idCounter = 0;
    private final int id = idCounter++;
    private final int collectionId;
    protected final String title;
    protected final int length;
    protected final LocalDate release;

    public PlayableItem(String title, int length, LocalDate release, int collectionId) {
        this.title = title;
        this.length = length;
        this.release = release;
        this.collectionId = collectionId;
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
