package ru.terra.universal.shared.persistance;

import ru.terra.universal.shared.entity.AbstractEntity;

import java.util.List;

/**
 * Date: 24.04.14
 * Time: 20:02
 */
public interface WorldLoader {
    List<AbstractEntity> load(final String worldUID);
}
