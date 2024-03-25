package models.audio.collections;

import models.audio.items.PlayableItem;
import services.UserService;

import java.util.ArrayList;
import java.util.List;

public abstract class AudioCollection<T extends PlayableItem> {
    private static int idCounter = 0;
    private final int id = idCounter++;
    private final int ownerId;
    protected final String name;
    protected List<T> items;

    public AudioCollection(String name) {
        this.name = name;
        this.ownerId = UserService.getInstance().getCurrentUser().getId();
        this.items = new ArrayList<>();
    }

    public int getId() {
        return id;
    }
    public int getOwnerId() {
        return ownerId;
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
        if (!items.contains(item)) {
            throw new IllegalArgumentException("Item not found");
        }
        items.remove(item);
    }
}
