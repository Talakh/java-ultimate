package org.course;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HeterogeneousMaxHolder {
    private final Map<Class<?>, Object> map = new HashMap<>();
    public <T extends Comparable<? super T>> void put(Class<T> clazz, T value) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(value);
        if (map.get(clazz) == null || value.compareTo(clazz.cast(map.get(clazz))) > 0) {
            map.put(clazz, value);
        }
    }

    public <T extends Comparable<? super T>> T getMax(Class<T> clazz) {
        return clazz.cast(map.get(clazz));
    }
}
