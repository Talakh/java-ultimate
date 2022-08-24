package org.course.session;

public record EntityKey<T>(Object id, Class<T> clazz) {
}
