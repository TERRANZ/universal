package ru.terra.universal.shared.core;

import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Класс используемый для поиска аннотированных классов
 */

public class ClassSearcher<T> {

    /**
     * Метод поиска аннотированных классов в
     * пакете @param packageName
     * по аннотации @param annotaion
     */
    public List<T> load(final String packageName, final Class annotaion) {
        final List<T> ret = new ArrayList<T>();
        final Reflections reflections = new Reflections(packageName);
        final Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(annotaion);
        for (final Class<?> c : annotated) {
            Object o = null;
            try {
                o = c.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            if (o != null)
                ret.add((T) o);
        }
        return ret;
    }
}