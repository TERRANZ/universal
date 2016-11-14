package ru.terra.universal.client.game.entity;

public class MapObject extends Entity {
    private long model;

    public MapObject(long guid, String name) {
        super(guid, name);
    }

    public long getModel() {
        return model;
    }

    public void setModel(long model) {
        this.model = model;
    }
}
