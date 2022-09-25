package org.course.pool;

import lombok.experimental.Delegate;

import java.sql.Connection;
import java.util.Queue;

public class ConnectionProxy implements Connection {

    @Delegate(excludes = Exclude.class)
    private final Connection connection;
    private final Queue<ConnectionProxy> connectionPool;

    public ConnectionProxy(final Connection connection, final Queue<ConnectionProxy> connectionPool) {
        this.connection = connection;
        this.connectionPool = connectionPool;
    }


    @Override
    public void close() {
        connectionPool.add(this);
    }

    private interface Exclude {
        void close();
    }

}
