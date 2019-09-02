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
    public List<T> load(String packageName, Class annotaion) {
        List<T> ret = new ArrayList<T>();
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(annotaion);
        for (Class<?> c : annotated) {
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