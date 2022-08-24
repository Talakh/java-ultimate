package org.course;

import org.course.session.SessionFactory;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

public class DemoApp {
    public static void main(String[] args) {
        var sf = new SessionFactory(postgresDataSource());

        var session1 = sf.createSession();
        var entity1 = session1.find(Product.class, 1);
        System.out.println(entity1);

        var entity2 = session1.find(Product.class, 2);
        System.out.println(entity2);

        var entity3 = session1.find(Product.class, 1);
        System.out.println(entity3);
        System.out.println(entity1 == entity3);

        var session2 = sf.createSession();
        var entity4 = session2.find(Product.class, 1);
        System.out.println(entity4);
        System.out.println(entity1 == entity4);

        session1.close();
        session2.close();
    }

    static DataSource postgresDataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL("jdbc:postgresql://localhost:5432/talakh");
        dataSource.setUser("talakh");
        dataSource.setPassword("talakh");

        return dataSource;
    }
}
