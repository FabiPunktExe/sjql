package de.fabiexe.sjql.database;

import de.fabiexe.sjql.Coffee;
import de.fabiexe.sjql.CompositeKey;
import de.fabiexe.sjql.Database;
import de.fabiexe.sjql.Table;
import de.fabiexe.sjql.column.Column;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractDatabaseTest {
    public record AutoIncrement(int id, String name) {
        public static final Table<AutoIncrement> TABLE = new Table<>(AutoIncrement.class, "auto_increment_test");
        public static final Column<Integer> ID = TABLE.intColumn("id").primaryKey();
        public static final Column<String> NAME = TABLE.stringColumn("name", 64).notNull();
    }

    protected Database database;

    protected abstract DataSource createDataSource() throws SQLException;

    @BeforeEach
    public void setup() throws SQLException {
        database = Objects.requireNonNull(Database.create(createDataSource()));
        database.createTable(Coffee.TABLE);
    }

    @AfterEach
    public void cleanup() throws SQLException {
        if (database.tableExists(Coffee.TABLE)) {
            database.deleteTable(Coffee.TABLE);
        }
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
    public void testInsertReturningGeneratedKey() throws SQLException {
        database.deleteTable(AutoIncrement.TABLE);
        database.createTable(AutoIncrement.TABLE);
        try {
            int firstId = database.throwingTransaction(() ->
                    AutoIncrement.TABLE.insertReturning(
                            AutoIncrement.ID,
                            row -> row.set(AutoIncrement.NAME, "first")
                    )
            );
            int secondId = database.throwingTransaction(() ->
                    AutoIncrement.TABLE.insertReturning(
                            AutoIncrement.ID,
                            row -> row.set(AutoIncrement.NAME, "second")
                    )
            );

            assertTrue(firstId > 0);
            assertTrue(secondId > firstId);
        } finally {
            database.deleteTable(AutoIncrement.TABLE);
        }
    }

    @Test
    public void testInsertReturningDefaultValue() throws SQLException {
        double price = database.throwingTransaction(() -> Coffee.TABLE.insertReturning(
                Coffee.PRICE,
                row -> row.set(Coffee.NAME, "Americano") // default price: 4.5
        ));
        assertEquals(4.5, price, 0.0001);
    }

    @Test
    public void testInsertReturningExplicitValue() throws SQLException {
        String name = database.throwingTransaction(() -> Coffee.TABLE.insertReturning(
                Coffee.NAME,
                row -> {
                    row.set(Coffee.NAME, "Latte");
                    row.set(Coffee.PRICE, 3.5);
                }
        ));
        assertEquals("Latte", name);
    }

    @Test
    public void testDefaultValue() throws SQLException {
        database.throwingTransaction(() -> {
            Coffee.TABLE.insert(row -> row.set(Coffee.NAME, "Americano")); // default price: 4.5
            List<Coffee> coffees = Coffee.TABLE.select().execute();
            assertEquals(List.of(new Coffee("Americano", 4.5)), coffees);
        });
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
                .where(Coffee.PRICE.neq(2.5))
                .execute());
        assertEquals(List.of(new Coffee("Latte", 3.5)), coffees);

        // AND
        coffees = database.throwingTransaction(() -> Coffee.TABLE.select()
                .where(Coffee.NAME.eq("Espresso").and(Coffee.PRICE.eq(2.5)))
                .execute());
        assertEquals(List.of(new Coffee("Espresso", 2.5)), coffees);

        // OR
        coffees = database.throwingTransaction(() -> Coffee.TABLE.select()
                .where(Coffee.NAME.eq("Espresso").or(Coffee.NAME.eq("Latte")))
                .execute());
        assertNotNull(coffees);
        assertEquals(2, coffees.size());

        // NOT
        coffees = database.throwingTransaction(() -> Coffee.TABLE.select()
                .where(Coffee.NAME.eq("Espresso").not())
                .execute());
        assertEquals(List.of(new Coffee("Latte", 3.5)), coffees);
    }

    @Test
    public void testUpdate() throws SQLException {
        database.throwingTransaction(() -> {
            Coffee.TABLE.insert(row -> {
                row.set(Coffee.NAME, "Espresso");
                row.set(Coffee.PRICE, 2.5);
            });

            Coffee.TABLE.update(Coffee.PRICE, 3.0)
                    .where(Coffee.NAME.eq("Espresso"))
                    .execute();

            List<Coffee> coffees = Coffee.TABLE.select().execute();
            assertEquals(List.of(new Coffee("Espresso", 3.0)), coffees);
        });
    }

    @Test
    public void testCompositePrimaryKey() throws SQLException {
        if (database.tableExists(CompositeKey.TABLE)) {
            database.deleteTable(CompositeKey.TABLE);
        }
        database.createTable(CompositeKey.TABLE);

        database.throwingTransaction(() -> {
            CompositeKey.TABLE.insert(row -> {
                row.set(CompositeKey.ID1, 1);
                row.set(CompositeKey.ID2, 1);
                row.set(CompositeKey.DATA, "A");
            });
            CompositeKey.TABLE.insert(row -> {
                row.set(CompositeKey.ID1, 1);
                row.set(CompositeKey.ID2, 2);
                row.set(CompositeKey.DATA, "B");
            });
        });

        assertEquals(2, database.throwingTransaction(CompositeKey.TABLE::count));

        // Duplicate key should fail
        assertThrows(SQLException.class, () -> {
            database.throwingTransaction(() -> {
                CompositeKey.TABLE.insert(row -> {
                    row.set(CompositeKey.ID1, 1);
                    row.set(CompositeKey.ID2, 1);
                    row.set(CompositeKey.DATA, "C");
                });
            });
        });
    }
}
