package org.course;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A generic comparator that is comparing a random field of the given class. The field is either primitive or
 * {@link Comparable}. It is chosen during comparator instance creation and is used for all comparisons.
 * <p>
 * By default it compares only accessible fields, but this can be configured via a constructor property. If no field is
 * available to compare, the constructor throws {@link IllegalArgumentException}
 *
 * @param <T> the type of the objects that may be compared by this comparator
 */
public class RandomFieldComparator<T> implements Comparator<T> {
    private Class<T> targetType;
    private Field fieldToCompare;

    public RandomFieldComparator(Class<T> targetType) {
        this(targetType, true);
    }

    /**
     * A constructor that accepts a class and a property indicating which fields can be used for comparison. If property
     * value is true, then only public fields or fields with public getters can be used.
     *
     * @param targetType                  a type of objects that may be compared
     * @param compareOnlyAccessibleFields config property indicating if only publicly accessible fields can be used
     */
    public RandomFieldComparator(Class<T> targetType, boolean compareOnlyAccessibleFields) {
        this.targetType = targetType;

        var fields = getAccessibleFields(targetType, compareOnlyAccessibleFields);
        if (fields.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.fieldToCompare = fields.get(ThreadLocalRandom.current().nextInt(fields.size()));
    }

    private List<Field> getAccessibleFields(Class<T> targetType, boolean compareOnlyAccessibleFields) {
        var fieldsStream = Arrays.stream(targetType.getDeclaredFields())
                .filter(this::isFieldComparable);

        if (compareOnlyAccessibleFields) {
            fieldsStream = fieldsStream.filter(this::isFieldAccessible);
        }

        return fieldsStream.toList();
    }

    private boolean isFieldComparable(Field field) {
        return field.getType().isPrimitive() || Comparable.class.isAssignableFrom(field.getType());
    }

    private boolean isFieldAccessible(Field field) {
        return isFieldPublic(field) || isHasPublicGetter(targetType, field);
    }

    private boolean isHasPublicGetter(Class<T> targetType, Field field) {
        return Arrays.stream(targetType.getDeclaredMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .anyMatch(method -> method.getName().equals(getGetterMethodName(field)));
    }

    private String getGetterMethodName(Field field) {
        return "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
    }

    private boolean isFieldPublic(Field field) {
        return Modifier.isPublic(field.getModifiers());
    }

    /**
     * Compares two objects of the class T by the value of the field that was randomly chosen. It allows null values
     * for the fields, and it treats null value grater than a non-null value (nulls last).
     */
    @Override
    public int compare(T o1, T o2) {
        Objects.requireNonNull(o1);
        Objects.requireNonNull(o2);

        return Comparator.<Comparable>nullsLast(Comparator.naturalOrder())
                .compare(getFieldValue(o1), getFieldValue(o2));
    }

    @SneakyThrows
    private Comparable getFieldValue(T o) {
        if (isFieldPublic(fieldToCompare)) {
            return (Comparable) fieldToCompare.get(o);
        } else {
            return (Comparable) targetType.getMethod(getGetterMethodName(fieldToCompare)).invoke(o);
        }
    }

    /**
     * Returns a statement "Random field comparator of class '%s' is comparing '%s'" where the first param is the name
     * of the type T, and the second parameter is the comparing field name.
     *
     * @return a predefined statement
     */
    @Override
    public String toString() {
        return String.format("Random field comparator of class '%s' is comparing '%s'", targetType.getName(), fieldToCompare.getName());
    }
}