package ru.terra.universal.shared.entity;

import java.io.Serializable;

public class PlayerInfo implements Serializable {
    private String name;
    private String world;

    public PlayerInfo(String name, String world) {
        this.name = name;
        this.world = world;
    }

    public PlayerInfo() {
        this.name = "";
        this.world = "";
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
