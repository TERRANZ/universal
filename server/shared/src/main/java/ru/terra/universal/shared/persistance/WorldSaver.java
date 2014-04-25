package ru.terra.universal.shared.persistance;

import ru.terra.universal.shared.entity.WorldEntity;

import java.util.List;

/**
 * Date: 24.04.14
 * Time: 20:03
 */
public interface WorldSaver {
    public void save(String worldUID, List<WorldEntity> entities);
}
