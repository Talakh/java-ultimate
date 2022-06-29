package org.course.backson;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

public class BacksonMapper {
    @SneakyThrows
    public <T> T readObject(String json, Class<T> tClass) {
        var fieldValues = Arrays.stream(json.replaceAll("[\\{|\\}]", "").split(","))
                .map(s -> s.split(":"))
                .collect(Collectors.toMap(
                        s -> s[0].replace("\"", "").trim(),
                        s -> s[1].replace("\"", "").trim()));

        T instance = tClass.getDeclaredConstructor().newInstance();

        for (Field f : tClass.getDeclaredFields()) {
            String jsonField = f.getName();
            if (f.isAnnotationPresent(PropertyName.class)) {
                jsonField = f.getAnnotation(PropertyName.class).value();
            }
            if (fieldValues.containsKey(jsonField)) {
                f.setAccessible(true);
                f.set(instance, fieldValues.get(jsonField));
            }
        }
        return instance;
    }
}
