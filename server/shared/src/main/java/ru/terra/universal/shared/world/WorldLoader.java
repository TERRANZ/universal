package ru.terra.universal.shared.world;

import ru.terra.universal.shared.entity.WorldEntity;

import java.util.List;

/**
 * Date: 24.04.14
 * Time: 20:02
 */
public interface WorldLoader {
    public List<WorldEntity> load(String worldUID);
}
