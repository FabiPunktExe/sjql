package de.fabiexe.sjql;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class H2Test {
    @Test
    public void test(@TempDir Path tempDir) throws SQLException {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:" + tempDir.resolve("test").toAbsolutePath());

        Database database = Database.create(dataSource);
        assertNotNull(database);

        assertFalse(database.tableExists(Coffee.TABLE));
        database.createTable(Coffee.TABLE);
        assertTrue(database.tableExists(Coffee.TABLE));

        long count = database.throwingTransaction(Coffee.TABLE::count);
        assertEquals(0, count);

        database.throwingTransaction(() -> {
            Coffee.TABLE.insert(row -> {
                row.set(Coffee.NAME, "Espresso");
                row.set(Coffee.PRICE, 2.5);
            });
        });

        List<Coffee> coffees = database.throwingTransaction(() -> Coffee.TABLE.select().execute());
        assertEquals(List.of(new Coffee("Espresso", 2.5)), coffees);
        assertNotEquals(List.of(new Coffee("Latte", 2.5)), coffees);
        assertNotEquals(List.of(new Coffee("Espresso", 1.5)), coffees);

        database.deleteTable(Coffee.TABLE);
        assertFalse(database.tableExists(Coffee.TABLE));
    }
}
