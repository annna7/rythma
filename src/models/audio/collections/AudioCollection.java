package models.audio.collections;

import models.audio.items.PlayableItem;

import java.util.ArrayList;
import java.util.List;

public abstract class AudioCollection<T extends PlayableItem> {
    protected final String name;
    protected List<T> items;

    public AudioCollection(String name) {
        this.name = name;
        this.items = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<T> getItems() {
        return items;
    }

    public void addItem(T item) {
        items.add(item);
    }

    public void removeItem(T item) {
        items.remove(item);
    }
}
