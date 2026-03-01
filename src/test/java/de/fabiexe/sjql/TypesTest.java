package de.fabiexe.sjql;

import de.fabiexe.sjql.column.Column;
import de.fabiexe.sjql.database.H2Database;
import de.fabiexe.sjql.expression.Expression;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TypesTest {
    public record AllTypes(
            int intVal,
            long longVal,
            float floatVal,
            double doubleVal,
            boolean boolVal,
            String stringVal,
            UUID uuidVal,
            Instant timestampVal
    ) {
        public static final Table<AllTypes> TABLE = new Table<>("all_types", AllTypes.class);
        public static final Column<Integer> INT = TABLE.intColumn("int_val");
        public static final Column<Long> LONG = TABLE.longColumn("long_val");
        public static final Column<Float> FLOAT = TABLE.floatColumn("float_val");
        public static final Column<Double> DOUBLE = TABLE.doubleColumn("double_val");
        public static final Column<Boolean> BOOL = TABLE.booleanColumn("bool_val");
        public static final Column<String> STRING = TABLE.stringColumn("string_val", 255);
        public static final Column<UUID> UUID = TABLE.uuidColumn("uuid_val");
        public static final Column<Instant> TIMESTAMP = TABLE.timestampColumn("timestamp_val");
    }

    private static Database database;

    @BeforeAll
    static void setup() throws SQLException {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        database = new H2Database(dataSource);
        database.createTable(AllTypes.TABLE);
    }

    @Test
    void testInsertAndSelect() throws SQLException {
        UUID uuid = UUID.randomUUID();
        Instant now = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        AllTypes item = new AllTypes(42, 123456789L, 3.14f, 2.71828, true, "Hello World", uuid, now);

        database.throwingTransaction(() -> {
            AllTypes.TABLE.insert(row -> {
                row.set(AllTypes.LONG, item.longVal());
                row.set(AllTypes.FLOAT, item.floatVal());
                row.set(AllTypes.DOUBLE, item.doubleVal());
                row.set(AllTypes.INT, item.intVal());
                row.set(AllTypes.STRING, item.stringVal());
                row.set(AllTypes.BOOL, item.boolVal());
                row.set(AllTypes.UUID, item.uuidVal());
                row.set(AllTypes.TIMESTAMP, item.timestampVal());
            });

            List<AllTypes> result = AllTypes.TABLE.select().execute();
            assertNotNull(result);

            AllTypes allTypes = result.getFirst();
            assertEquals(item.intVal(), allTypes.intVal());
            assertEquals(item.longVal(), allTypes.longVal());
            assertEquals(item.floatVal(), allTypes.floatVal(), 0.0001);
            assertEquals(item.doubleVal(), allTypes.doubleVal(), 0.0000001);
            assertEquals(item.boolVal(), allTypes.boolVal());
            assertEquals(item.stringVal(), allTypes.stringVal());
            assertEquals(item.uuidVal(), allTypes.uuidVal());
            assertEquals(item.timestampVal(), allTypes.timestampVal());
        });
    }

    @Test
    void testInsertCurrentTimestamp() throws SQLException {
        database.throwingTransaction(() -> {
            AllTypes.TABLE.insert(row -> {
                row.set(AllTypes.LONG, 1L);
                row.set(AllTypes.FLOAT, 1.0f);
                row.set(AllTypes.DOUBLE, 1.0);
                row.set(AllTypes.INT, 1);
                row.set(AllTypes.STRING, "test");
                row.set(AllTypes.BOOL, true);
                row.set(AllTypes.UUID, UUID.randomUUID());
                row.set(AllTypes.TIMESTAMP, Expression.currentTimestamp());
            });

            List<AllTypes> result = AllTypes.TABLE.select().execute();
            assertNotNull(result);
            assertNotNull(result.getFirst().timestampVal());
        });
    }
}
