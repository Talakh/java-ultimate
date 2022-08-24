package org.course.session;

import lombok.SneakyThrows;
import org.course.annotations.Column;
import org.course.annotations.Table;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class Session {
    private static final String SELECT_QUERY = "SELECT * FROM %s WHERE id = ?";
    private final DataSource dataSource;
    private final Map<EntityKey<?>, Object> cache = new HashMap<>();

    public Session(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> T find(Class<T> entityType, Object id) {
        var entityKey = new EntityKey<>(id, entityType);
        var entity = cache.computeIfAbsent(entityKey, a -> loadFromDB(entityType, id));
        return entityType.cast(entity);
    }

    @SneakyThrows
    private <T> T loadFromDB(Class<T> entityType, Object id) {
        try (var connection = dataSource.getConnection();
             var ps = connection.prepareStatement(createSelectQuery(entityType))) {
            ps.setObject(1, id);
            var rs = ps.executeQuery();
            return mapResultSetToEntity(rs, entityType);
        }
    }

    @SneakyThrows
    private <T> T mapResultSetToEntity(ResultSet rs, Class<T> entityType) {
        rs.next();
        var entity = entityType.getDeclaredConstructor().newInstance();
        for (Field f : entityType.getDeclaredFields()) {
            f.setAccessible(true);
            var fieldValue = rs.getObject(getColumnName(f));
            f.set(entity, fieldValue);
        }
        return entity;
    }

    private <T> String createSelectQuery(Class<T> entityType) {
        var annotation = entityType.getAnnotation(Table.class);
        var tableName = annotation.value();
        var query = String.format(SELECT_QUERY, tableName);
        System.out.println("SQL: " + query);
        return query;
    }

    private String getColumnName(Field f) {
        if (f.isAnnotationPresent(Column.class)) {
            var annotations = f.getAnnotation(Column.class);
            var columnValue = annotations.value();
            return columnValue.isEmpty() ? f.getName() : columnValue;
        }
        return f.getName();
    }

    public void close() {
        cache.clear();
    }
}
