package ru.terra.universal.shared.entity;

/**
 * Date: 13.01.14
 * Time: 14:42
 */
public class WorldEntityInfo extends AbstractEntity {
    private Integer model = 0;
    private Integer state = 0;

    public Integer getModel() {
        return model;
    }

    public void setModel(Integer model) {
        this.model = model;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "WorldEntityInfo{" +
                "model=" + model +
                ", state=" + state +
                "} " + super.toString();
    }
}
