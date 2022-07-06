package org.course.context;

import lombok.SneakyThrows;
import org.course.annotations.Bean;
import org.course.exceptions.NoSuchBeanException;
import org.course.exceptions.NoUniqueBeanException;
import org.reflections.Reflections;

import java.util.Map;

import static java.util.stream.Collectors.toConcurrentMap;
import static java.util.stream.Collectors.toMap;

public class ApplicationContextImpl implements ApplicationContext {
    private final Map<String, Object> beansMap;

    public ApplicationContextImpl(String packageName) {
        Reflections reflections = new Reflections(packageName);
        beansMap = reflections.getTypesAnnotatedWith(Bean.class).stream()
                .collect(toConcurrentMap(this::getBeanName, this::createBean));
    }

    @Override
    public <T> T getBean(Class<T> beanType) {
        var beans = beansMap.entrySet().stream()
                .filter(entry -> beanType.isAssignableFrom(entry.getValue().getClass()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        validateFoundBeans(beans, beanType);

        return beanType.cast(beans.get(getBeanName(beanType)));
    }

    @Override
    public <T> T getBean(String name, Class<T> beanType) {
        var beans = beansMap.entrySet().stream()
                .filter(entry -> entry.getKey().equals(name))
                .filter(entry -> beanType.isAssignableFrom(entry.getValue().getClass()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        validateFoundBeans(beans, beanType);

        return beanType.cast(beans.get(name));
    }

    @Override
    public <T> Map<String, T> getAllBeans(Class<T> beanType) {
        return beansMap.entrySet().stream()
                .filter(entry -> beanType.isAssignableFrom(entry.getValue().getClass()))
                .collect(toMap(Map.Entry::getKey, entry -> beanType.cast(entry.getValue())));
    }

    private void validateFoundBeans(Map<String, Object> beans, Class<?> beanType) {
        if (beans.isEmpty()) {
            throw new NoSuchBeanException("Bean of type %s not found!".formatted(beanType.getName()));
        }
        if (beans.size() > 1) {
            throw new NoUniqueBeanException("More than one bean found for type %s: %s".formatted(beanType.getName(), beans.keySet()));
        }
    }

    private String getBeanName(Class<?> clazz) {
        var annotation = clazz.getAnnotation(Bean.class);
        var clazzName = clazz.getSimpleName();
        return annotation.value().isEmpty() ?
                clazzName.substring(0, 1).toLowerCase() + clazzName.substring(1) :
                annotation.value();
    }

    @SneakyThrows
    private Object createBean(Class<?> clazz) {
        return clazz.getDeclaredConstructor().newInstance();
    }
}
