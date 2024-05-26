package models.audio.collections;

import exceptions.NotFoundException;
import models.audio.items.PlayableItem;
import services.UserService;

import java.util.ArrayList;
import java.util.List;

public abstract class AudioCollection<T extends PlayableItem> {
    protected int id;
    protected int ownerId;
    protected String name;
    protected List<T> items = new ArrayList<>();

    public AudioCollection(String name) {
        this.name = name;
        this.ownerId = UserService.getInstance().getCurrentUser().getId();
        this.items = new ArrayList<>();
    }

    public AudioCollection(int id, int ownerId, String name) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
    }

    private AudioCollection(int id, int ownerId, String name, List<T> items) {
        this(id, ownerId, name);
        this.items = items;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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
            throw new NotFoundException(String.format("Item %s not found", item));
        }
        items.remove(item);
    }
}
