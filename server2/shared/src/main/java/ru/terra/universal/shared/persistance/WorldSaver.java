package ru.terra.universal.shared.persistance;

import ru.terra.universal.shared.entity.AbstractEntity;

import java.util.List;

/**
 * Date: 24.04.14
 * Time: 20:03
 */
public interface WorldSaver {
    void save(final String worldUID, final List<AbstractEntity> entities);
}
