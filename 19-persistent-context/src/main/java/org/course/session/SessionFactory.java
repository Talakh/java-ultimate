package org.course.session;

import javax.sql.DataSource;

public class SessionFactory {
    private final DataSource dataSource;

    public SessionFactory(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Session createSession() {
        return new Session(dataSource);
    }
}
