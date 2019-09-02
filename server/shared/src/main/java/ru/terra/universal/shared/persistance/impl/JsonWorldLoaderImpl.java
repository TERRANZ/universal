package ru.terra.universal.shared.persistance.impl;

import ru.terra.universal.shared.entity.AbstractEntity;
import ru.terra.universal.shared.persistance.WorldLoader;

import java.util.LinkedList;
import java.util.List;

/**
 * Date: 25.04.14
 * Time: 14:15
 */
public class JsonWorldLoaderImpl implements WorldLoader {
    @Override
    public List<AbstractEntity> load(String worldUID) {
        return new LinkedList<>();
    }
}
