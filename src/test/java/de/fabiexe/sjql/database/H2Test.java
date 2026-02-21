package de.fabiexe.sjql.database;

import org.h2.jdbcx.JdbcDataSource;

public class H2Test extends AbstractDatabaseTest {
    @Override
    protected JdbcDataSource createDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        return dataSource;
    }
}
