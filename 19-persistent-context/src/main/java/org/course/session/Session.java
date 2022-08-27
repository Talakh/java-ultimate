package org.course.session;

import lombok.SneakyThrows;
import org.course.utils.EntityUtils;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.course.utils.EntityUtils.getColumnName;
import static org.course.utils.EntityUtils.getIdField;
import static org.course.utils.EntityUtils.getInitialColumnValues;
import static org.course.utils.EntityUtils.getSortedEntityFields;
import static org.course.utils.EntityUtils.getTableName;

public class Session {
    private static final String SELECT_QUERY = "SELECT * FROM %s WHERE id = ?";
    private static final String UPDATE_QUERY = "UPDATE %s SET %s WHERE id = ?";
    private final DataSource dataSource;
    private final Map<EntityKey<?>, Object> cache = new HashMap<>();
    private final Map<EntityKey<?>, List<Object>> dirtyCache = new HashMap<>();

    public Session(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> T find(Class<T> entityType, Object id) {
        var entityKey = new EntityKey<>(id, entityType);
        var entity = cache.computeIfAbsent(entityKey, a -> {
            var dbEntity = loadFromDB(entityType, id);
            dirtyCache.put(entityKey, getInitialColumnValues(entityType, dbEntity));
            return dbEntity;
        });
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
        var tableName = getTableName(entityType);
        var query = String.format(SELECT_QUERY, tableName);
        System.out.println("SQL: " + query);
        return query;
    }

    @SneakyThrows
    public void close() {
        for (Map.Entry<EntityKey<?>, List<Object>> entry : dirtyCache.entrySet()) {
            var entity = cache.get(entry.getKey());
            var fields = getSortedEntityFields(entry.getKey().clazz());
            List<Field> changedFields = new ArrayList<>();
            for (int i = 0; i < fields.size(); i++) {
                var field = fields.get(i);
                field.setAccessible(true);
                if (!Objects.equals(field.get(entity), entry.getValue().get(i))) {
                    changedFields.add(field);
                }
            }
            if (!changedFields.isEmpty()) {
                var values = changedFields.stream()
                        .map(EntityUtils::getColumnName)
                        .map(name -> name + " = ?")
                        .collect(Collectors.joining(", "));

                String query = String.format(UPDATE_QUERY, getTableName(entry.getKey().clazz()), values);
                try (var connection = dataSource.getConnection();
                     var ps = connection.prepareStatement(query)) {
                    for (int i = 1; i < changedFields.size() + 1; i++) {
                        ps.setObject(i, changedFields.get(i - 1).get(entity));
                    }
                    var idField = getIdField(entry.getKey().clazz());
                    idField.setAccessible(true);
                    Object id = idField.get(entity);
                    ps.setObject(changedFields.size() + 1, id);
                    ps.executeUpdate();
                }
            }

        }
        cache.clear();
        dirtyCache.clear();
    }
}
