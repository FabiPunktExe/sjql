package de.fabiexe.sjql;

import de.fabiexe.sjql.column.Column;
import de.fabiexe.sjql.database.H2Database;
import org.h2.jdbcx.JdbcDataSource;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UniqueTest {

    public record SingleUnique(int id, String email) {
        public static final Table<SingleUnique> TABLE = new Table<>(SingleUnique.class, "single_unique");
        public static final Column<Integer> ID = TABLE.intColumn("id").primaryKey();
        public static final Column<String> EMAIL = TABLE.stringColumn("email", 255).unique();
    }

    public record CompositeUnique(int id, String firstName, String lastName) {
        public static final Table<CompositeUnique> TABLE = new Table<>(CompositeUnique.class, "composite_unique");
        public static final Column<Integer> ID = TABLE.intColumn("id").primaryKey();
        public static final Column<String> FIRST_NAME = TABLE.stringColumn("first_name", 64).notNull();
        public static final Column<String> LAST_NAME = TABLE.stringColumn("last_name", 64).notNull();

        static {
            TABLE.unique(FIRST_NAME, LAST_NAME);
        }
    }

    private static @Nullable Database database = null;

    @BeforeAll
    static void setup() throws SQLException {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:unique_test;DB_CLOSE_DELAY=-1");
        database = new H2Database(dataSource);
        Objects.requireNonNull(database).createTable(SingleUnique.TABLE);
        Objects.requireNonNull(database).createTable(CompositeUnique.TABLE);
    }

    @Test
    void testSingleColumnUnique() throws SQLException {
        Objects.requireNonNull(database).deleteTable(SingleUnique.TABLE);
        database.createTable(SingleUnique.TABLE);

        database.throwingTransaction(() -> {
            SingleUnique.TABLE.insert(row -> {
                row.set(SingleUnique.ID, 1);
                row.set(SingleUnique.EMAIL, "alice@example.com");
            });
            SingleUnique.TABLE.insert(row -> {
                row.set(SingleUnique.ID, 2);
                row.set(SingleUnique.EMAIL, "bob@example.com");
            });
        });

        List<SingleUnique> result = database.throwingTransaction(() -> SingleUnique.TABLE.select().orderBy(SingleUnique.ID, true).execute());
        assertEquals(List.of(
                new SingleUnique(1, "alice@example.com"),
                new SingleUnique(2, "bob@example.com")
        ), result);

        assertThrows(SQLException.class, () -> Objects.requireNonNull(database).throwingTransaction(() -> {
            SingleUnique.TABLE.insert(row -> {
                row.set(SingleUnique.ID, 3);
                row.set(SingleUnique.EMAIL, "alice@example.com");
            });
        }));
    }

    @Test
    void testCompositeUnique() throws SQLException {
        Objects.requireNonNull(database).deleteTable(CompositeUnique.TABLE);
        database.createTable(CompositeUnique.TABLE);

        database.throwingTransaction(() -> {
            CompositeUnique.TABLE.insert(row -> {
                row.set(CompositeUnique.ID, 1);
                row.set(CompositeUnique.FIRST_NAME, "Alice");
                row.set(CompositeUnique.LAST_NAME, "Smith");
            });
            CompositeUnique.TABLE.insert(row -> {
                row.set(CompositeUnique.ID, 2);
                row.set(CompositeUnique.FIRST_NAME, "Alice");
                row.set(CompositeUnique.LAST_NAME, "Jones");
            });
            CompositeUnique.TABLE.insert(row -> {
                row.set(CompositeUnique.ID, 3);
                row.set(CompositeUnique.FIRST_NAME, "Bob");
                row.set(CompositeUnique.LAST_NAME, "Smith");
            });
        });

        assertEquals(3, database.throwingTransaction(CompositeUnique.TABLE::count));

        assertThrows(SQLException.class, () -> Objects.requireNonNull(database).throwingTransaction(() -> {
            CompositeUnique.TABLE.insert(row -> {
                row.set(CompositeUnique.ID, 4);
                row.set(CompositeUnique.FIRST_NAME, "Alice");
                row.set(CompositeUnique.LAST_NAME, "Smith");
            });
        }));
    }

    @Test
    void testUniqueConstraintValidation() {
        Table<?> table = new Table<>(Object.class, "validation_table");
        Column<@Nullable String> column = table.stringColumn("col", 64);

        assertThrows(IllegalArgumentException.class, table::unique);
        assertThrows(IllegalArgumentException.class, () -> {
            Table<?> otherTable = new Table<>(Object.class, "other_table");
            Column<@Nullable String> otherColumn = otherTable.stringColumn("other_col", 64);
            table.unique(otherColumn);
        });
        assertThrows(IllegalArgumentException.class, () -> table.unique(column, column));
    }
}