package models.audio.items;

import java.time.LocalDate;

public abstract class PlayableItem {
    protected int id;
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

    public LocalDate getRelease() {
        return release;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCollectionId() {
        return collectionId;
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
}
