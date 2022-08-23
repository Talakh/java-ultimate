package org.course.pool;

import lombok.experimental.Delegate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PooledDataSource implements DataSource {
    private static final int DEFAULT_CONNECTIONS_COUNT = 20;
    private final Queue<ConnectionProxy> connectionPool = new ConcurrentLinkedQueue<>();
    @Delegate(excludes = Exclude.class)
    private final DataSource dataSource;

    public PooledDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
        init();
    }

    private void init() {
        for (int i = 0; i < DEFAULT_CONNECTIONS_COUNT; i++) {
            try {
                connectionPool.add(new ConnectionProxy(dataSource.getConnection(), connectionPool));
            } catch (SQLException e) {
                throw new RuntimeException("Cant get connection from datasource", e);
            }
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connectionPool.poll();
    }

    private interface Exclude {
        Connection getConnection();
    }
}
