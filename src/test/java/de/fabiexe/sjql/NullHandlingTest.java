package de.fabiexe.sjql;

import de.fabiexe.sjql.column.Column;
import de.fabiexe.sjql.database.H2Database;
import org.h2.jdbcx.JdbcDataSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import static org.junit.jupiter.api.Assertions.*;

public class NullHandlingTest {
    private Database database;
    private DataSource dataSource;

    public record TestRecord(int id, @NotNull String name, @Nullable String description) {
        public static final Table<TestRecord> TABLE = new Table<>("test_nulls", TestRecord.class);
        public static final Column<@NotNull Integer> ID = TABLE.intColumn("id").primaryKey();
        public static final Column<@NotNull String> NAME = TABLE.stringColumn("name", 64).notNull();
        public static final Column<@Nullable String> DESCRIPTION = TABLE.stringColumn("description", 64);
    }

    @BeforeEach
    public void setup() throws SQLException {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test_nulls;DB_CLOSE_DELAY=-1");
        this.dataSource = ds;
        database = new H2Database(ds);
        database.createTable(TestRecord.TABLE);
    }

    @Test
    public void testInsertWithNull() throws SQLException {
        database.throwingTransaction(() -> {
            assertDoesNotThrow(() -> {
                TestRecord.TABLE.insert(row -> {
                    row.set(TestRecord.ID, 1);
                    row.set(TestRecord.NAME, "Test");
                });
            });

            // Verify the data
            TestRecord record = TestRecord.TABLE.select()
                    .where(TestRecord.ID.eq(1))
                    .executeNotNull()
                    .getFirst();
            assertEquals("Test", record.name());
            assertNull(record.description());
        });
    }

    @Test
    public void testCreateTableSql() {
        database.transaction(() -> {
            assertThrows(SQLException.class, () -> {
                // Use a raw SQL to bypass sjql's validation
                try (Connection connection = dataSource.getConnection();
                     PreparedStatement statement = connection.prepareStatement("""
                             INSERT INTO test_nulls (id, name, description)
                             VALUES (?, ?, ?)""")) {
                    statement.setInt(1, 10);
                    statement.setNull(2, Types.VARCHAR); // name is NOT NULL
                    statement.setString(3, "Desc");
                    statement.executeUpdate();
                }
            });
        });
    }

    @Test
    public void testNotNullConstraint() {
        database.transaction(() -> {
            assertThrows(IllegalArgumentException.class, () -> {
                TestRecord.TABLE.insert(row -> {
                    row.set(TestRecord.ID, 2);
                    row.set(TestRecord.DESCRIPTION, "Description");
                });
            });

            assertThrows(IllegalArgumentException.class, () -> {
                TestRecord.TABLE.insert(row -> {
                    row.set(TestRecord.ID, 3);
                    row.set(TestRecord.NAME, (String) null);
                });
            });
        });
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testUpdateNotNullToNull() throws SQLException {
        database.throwingTransaction(() -> {
            TestRecord.TABLE.insert(row -> {
                row.set(TestRecord.ID, 4);
                row.set(TestRecord.NAME, "Original");
            });

            assertThrows(IllegalArgumentException.class, () -> {
                TestRecord.TABLE.update(TestRecord.NAME, null)
                        .where(TestRecord.ID.eq(4))
                        .execute();
            });
        });
    }
}
