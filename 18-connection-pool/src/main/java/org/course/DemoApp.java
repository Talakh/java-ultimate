package org.course;

import lombok.SneakyThrows;
import org.course.pool.PooledDataSource;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class DemoApp {

    @SneakyThrows
    public static void main(String[] args) {
        var dataSource = pooledDataSource(postgresDataSource());
        var start = System.nanoTime();
        for (int i = 0; i < 5000; i++) {
            try (var connection = dataSource.getConnection()) {
                connection.setAutoCommit(false);
                try (var statement = connection.createStatement()) {
                    var rs = statement.executeQuery("select random() from PRODUCTS");
                    rs.next();
                }
                connection.rollback();
            }
        }

        System.out.println(TimeUnit.of(ChronoUnit.NANOS).toMillis(System.nanoTime() - start));
    }

    static DataSource postgresDataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL("jdbc:postgresql://localhost:5432/talakh");
        dataSource.setUser("talakh");
        dataSource.setPassword("talakh");

        return dataSource;
    }

    static DataSource pooledDataSource(DataSource dataSource) {
        return new PooledDataSource(dataSource);
    }
}
