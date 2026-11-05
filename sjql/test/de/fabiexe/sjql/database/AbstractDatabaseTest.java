package de.fabiexe.sjql.database;

import de.fabiexe.sjql.*;
import de.fabiexe.sjql.column.Column;
import de.fabiexe.sjql.expression.Expression;
import de.fabiexe.sjql.row.ReadableRow;
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
        if (database.tableExists(Purchase.TABLE)) {
            database.deleteTable(Purchase.TABLE);
        }
        if (database.tableExists(Employee.TABLE)) {
            database.deleteTable(Employee.TABLE);
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

    @Test
    public void testImplicitJoinWithAliases() throws SQLException {
        database.createTable(Purchase.TABLE);
        database.throwingTransaction(() -> {
            insertCoffee("Espresso", 2.5);
            insertCoffee("Latte", 3.5);
            insertPurchase(1, "Espresso", 2);
            insertPurchase(2, "Latte", 1);
            insertPurchase(3, "Espresso", 5);
        });

        TableRef<Coffee> coffee = Coffee.TABLE.alias("c");
        TableRef<Purchase> purchase = Purchase.TABLE.alias("p");
        List<ReadableRow> rows = database.throwingTransaction(() -> coffee.combine(purchase)
                .where(coffee.column(Coffee.NAME).eq(purchase.column(Purchase.COFFEE_NAME)))
                .orderBy(coffee.column(Coffee.NAME), true)
                .orderBy(purchase.column(Purchase.AMOUNT), true)
                .executeNotNull());

        assertEquals(3, rows.size());
        assertEquals("Espresso", rows.get(0).get(coffee.column(Coffee.NAME)));
        assertEquals(2, rows.get(0).get(purchase.column(Purchase.AMOUNT)).intValue());
        assertEquals("Espresso", rows.get(1).get(coffee.column(Coffee.NAME)));
        assertEquals(5, rows.get(1).get(purchase.column(Purchase.AMOUNT)).intValue());
        assertEquals("Latte", rows.get(2).get(coffee.column(Coffee.NAME)));
    }

    @Test
    public void testImplicitJoinAutoQualifiesPlainColumns() throws SQLException {
        database.createTable(Purchase.TABLE);
        database.throwingTransaction(() -> {
            insertCoffee("Espresso", 2.5);
            insertPurchase(1, "Espresso", 2);
        });

        List<ReadableRow> rows = database.throwingTransaction(() ->
                Coffee.TABLE.ref().combine(Purchase.TABLE.ref())
                        .where(Coffee.NAME.eq(Purchase.COFFEE_NAME))
                        .executeNotNull());

        assertEquals(1, rows.size());
        assertEquals("Espresso", rows.getFirst().get(Coffee.NAME));
        assertEquals(2, rows.getFirst().get(Purchase.AMOUNT).intValue());
    }

    @Test
    public void testSelfJoinWithAliases() throws SQLException {
        database.createTable(Employee.TABLE);
        database.throwingTransaction(() -> {
            Employee.TABLE.insert(row -> {
                row.set(Employee.ID, 1);
                row.set(Employee.NAME, "Alice");
            });
            Employee.TABLE.insert(row -> {
                row.set(Employee.ID, 2);
                row.set(Employee.NAME, "Bob");
                row.set(Employee.MANAGER_ID, 1);
            });
        });

        TableRef<Employee> employee = Employee.TABLE.alias("e");
        TableRef<Employee> manager = Employee.TABLE.alias("m");
        List<ReadableRow> rows = database.throwingTransaction(() -> employee.combine(manager)
                .where(employee.column(Employee.MANAGER_ID).eq(manager.column(Employee.ID)))
                .executeNotNull());

        assertEquals(1, rows.size());
        assertEquals("Bob", rows.getFirst().get(employee.column(Employee.NAME)));
        assertEquals("Alice", rows.getFirst().get(manager.column(Employee.NAME)));
        assertThrows(IllegalArgumentException.class, () -> rows.getFirst().get(Employee.NAME));
    }

    @Test
    public void testJoinCountOrderByAndLimit() throws SQLException {
        database.createTable(Purchase.TABLE);
        database.throwingTransaction(() -> {
            insertCoffee("Espresso", 2.5);
            insertCoffee("Latte", 3.5);
            insertPurchase(1, "Espresso", 2);
            insertPurchase(2, "Latte", 1);
            insertPurchase(3, "Espresso", 5);
        });

        TableRef<Coffee> coffee = Coffee.TABLE.alias("c");
        TableRef<Purchase> purchase = Purchase.TABLE.alias("p");
        Expression joinCondition = coffee.column(Coffee.NAME).eq(purchase.column(Purchase.COFFEE_NAME));

        long count = database.throwingTransaction(() ->
                coffee.combine(purchase).where(joinCondition).count().executeNotNull());
        assertEquals(3, count);

        List<ReadableRow> rows = database.throwingTransaction(() ->
                coffee.combine(purchase)
                        .where(joinCondition)
                        .orderBy(coffee.column(Coffee.NAME), true)
                        .limit(2L)
                        .executeNotNull());

        assertEquals(2, rows.size());
        assertEquals("Espresso", rows.getFirst().get(coffee.column(Coffee.NAME)));
    }

    @Test
    public void testJoinValidation() {
        assertThrows(IllegalArgumentException.class, () ->
                database.throwingTransaction(() -> Coffee.TABLE.alias("x").combine(Coffee.TABLE.alias("x"))));
        assertThrows(IllegalArgumentException.class, () ->
                database.throwingTransaction(() -> Coffee.TABLE.ref().combine(Coffee.TABLE.ref())));
        assertThrows(IllegalArgumentException.class, () ->
                database.throwingTransaction(() -> Purchase.TABLE.alias("coffee").combine(Coffee.TABLE.ref())));
        assertThrows(IllegalArgumentException.class, () -> Coffee.TABLE.alias(" "));
        assertThrows(IllegalArgumentException.class, () -> Coffee.TABLE.alias("c").column(Purchase.COFFEE_NAME));
    }

    private void insertCoffee(String name, double price) throws SQLException {
        Coffee.TABLE.insert(row -> {
            row.set(Coffee.NAME, name);
            row.set(Coffee.PRICE, price);
        });
    }

    private void insertPurchase(int id, String coffeeName, int amount) throws SQLException {
        Purchase.TABLE.insert(row -> {
            row.set(Purchase.ID, id);
            row.set(Purchase.COFFEE_NAME, coffeeName);
            row.set(Purchase.AMOUNT, amount);
        });
    }
}
