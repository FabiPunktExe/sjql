package de.fabiexe.sjql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractDatabaseTest {
    protected Database database;

    protected abstract DataSource createDataSource() throws SQLException;

    @BeforeEach
    public void setup() throws SQLException {
        database = Objects.requireNonNull(Database.create(createDataSource()));
        if (database.tableExists(Coffee.TABLE)) {
            database.deleteTable(Coffee.TABLE);
        }
        database.createTable(Coffee.TABLE);
    }

    @Test
    public void testInsertAndSelect() throws SQLException {
        database.throwingTransaction(() -> {
            Coffee.TABLE.insert(row -> {
                row.set(Coffee.NAME, "Espresso");
                row.set(Coffee.PRICE, 2.5);
            });
        });

        List<Coffee> coffees = database.throwingTransaction(() -> Coffee.TABLE.select().execute());
        assertEquals(List.of(new Coffee("Espresso", 2.5)), coffees);
    }

    @Test
    public void testCount() throws SQLException {
        assertEquals(0, database.throwingTransaction(Coffee.TABLE::count));

        database.throwingTransaction(() -> {
            Coffee.TABLE.insert(row -> {
                row.set(Coffee.NAME, "Espresso");
                row.set(Coffee.PRICE, 2.5);
            });
        });

        assertEquals(1, database.throwingTransaction(Coffee.TABLE::count));
    }

    @Test
    public void testDelete() throws SQLException {
        database.throwingTransaction(() -> {
            Coffee.TABLE.insert(row -> {
                row.set(Coffee.NAME, "Espresso");
                row.set(Coffee.PRICE, 2.5);
            });
        });

        database.throwingTransaction(() -> {
            Coffee.TABLE.delete()
                    .where(Coffee.NAME.eq("Espresso"))
                    .execute();
        });

        assertEquals(0, database.throwingTransaction(Coffee.TABLE::count));
    }

    @Test
    public void testLogicalOperators() throws SQLException {
        database.throwingTransaction(() -> {
            Coffee.TABLE.insert(row -> {
                row.set(Coffee.NAME, "Espresso");
                row.set(Coffee.PRICE, 2.5);
            });
            Coffee.TABLE.insert(row -> {
                row.set(Coffee.NAME, "Latte");
                row.set(Coffee.PRICE, 3.5);
            });
        });

        // ==
        List<Coffee> coffees = database.throwingTransaction(() -> Coffee.TABLE.select()
                .where(Coffee.PRICE.eq(2.5))
                .execute());
        assertEquals(List.of(new Coffee("Espresso", 2.5)), coffees);

        // >
        coffees = database.throwingTransaction(() -> Coffee.TABLE.select()
                .where(Coffee.PRICE.gt(3.0))
                .execute());
        assertEquals(List.of(new Coffee("Latte", 3.5)), coffees);

        // <
        coffees = database.throwingTransaction(() -> Coffee.TABLE.select()
                .where(Coffee.PRICE.lt(3.0))
                .execute());
        assertEquals(List.of(new Coffee("Espresso", 2.5)), coffees);

        // <=
        coffees = database.throwingTransaction(() -> Coffee.TABLE.select()
                .where(Coffee.PRICE.lte(2.5))
                .execute());
        assertEquals(List.of(new Coffee("Espresso", 2.5)), coffees);

        // !=
        coffees = database.throwingTransaction(() -> Coffee.TABLE.select()
                .where(Coffee.PRICE.ne(2.5))
                .execute());
        assertEquals(List.of(new Coffee("Latte", 3.5)), coffees);
    }
}
