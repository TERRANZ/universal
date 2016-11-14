package ru.terra.universal.client.game.entity;

import ru.terra.universal.shared.entity.EntityCoordinates;

public abstract class Entity extends EntityCoordinates {
    private String name;
    private long guid;

    public Entity(long guid, String name) {
        this.guid = guid;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getGuid() {
        return guid;
    }

    public void setGuid(long guid) {
        this.guid = guid;
    }

    public void setPosition(float x, float y, float z, float h) {
        setX(x);
        setY(y);
        setZ(z);
        setH(h);
    }
}
