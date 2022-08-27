package org.course.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.course.annotations.Column;
import org.course.annotations.Id;
import org.course.annotations.Table;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@UtilityClass
public class EntityUtils {
    public static List<Field> getSortedEntityFields(Class<?> entityType) {
        return Arrays.stream(entityType.getDeclaredFields())
                .sorted(Comparator.comparing(Field::getName))
                .toList();
    }

    @SneakyThrows
    public static <T> List<Object> getInitialColumnValues(Class<T> entityType, T dbEntity) {
        List<Field> fields = getSortedEntityFields(entityType);
        List<Object> objectFieldValues = new ArrayList<>();
        for (Field f : fields) {
            f.setAccessible(true);
            objectFieldValues.add(f.get(dbEntity));
        }
        return objectFieldValues;
    }

    public static Field getIdField(Class<?> entityType) {
        return Arrays.stream(entityType.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Id.class))
                .findAny()
                .orElseThrow();
    }

    public static String getTableName(Class<?> entityType) {
        var annotation = entityType.getAnnotation(Table.class);
        return annotation.value();
    }

    public static String getColumnName(Field f) {
        if (f.isAnnotationPresent(Column.class)) {
            var annotations = f.getAnnotation(Column.class);
            var columnValue = annotations.value();
            return columnValue.isEmpty() ? f.getName() : columnValue;
        }
        return f.getName();
    }
}
