package de.fabiexe.sjql;

import de.fabiexe.sjql.database.H2Database;
import de.fabiexe.sjql.expression.Expression;
import de.fabiexe.sjql.util.SQLUtil;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SqlGenerationTest {
    private H2Database database;

    @BeforeEach
    public void setup() {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test_nulls;DB_CLOSE_DELAY=-1");
        database = new H2Database(ds);
    }

    @Test
    public void testLogicalOperators() {
        assertEquals("(\"name\") = (?)", SQLUtil.buildSql(database, Coffee.NAME.eq("Espresso")).getKey());
        assertEquals("(\"name\") != (?)", SQLUtil.buildSql(database, Coffee.NAME.neq("Espresso")).getKey());
        assertEquals("(\"price\") > (?)", SQLUtil.buildSql(database, Coffee.PRICE.gt(2.0)).getKey());
        assertEquals("(\"price\") >= (?)", SQLUtil.buildSql(database, Coffee.PRICE.gte(2.5)).getKey());
        assertEquals("(\"price\") < (?)", SQLUtil.buildSql(database, Coffee.PRICE.lt(3.0)).getKey());
        assertEquals("(\"price\") <= (?)", SQLUtil.buildSql(database, Coffee.PRICE.lte(2.5)).getKey());
        assertEquals("((\"name\") = (?)) AND ((\"price\") > (?))", SQLUtil.buildSql(database, Coffee.NAME.eq("Espresso").and(Coffee.PRICE.gt(2.0))).getKey());
        assertEquals("((\"name\") = (?)) OR ((\"price\") > (?))", SQLUtil.buildSql(database, Coffee.NAME.eq("Espresso").or(Coffee.PRICE.gt(2.0))).getKey());
        assertEquals("((\"name\") = (?)) XOR ((\"price\") > (?))", SQLUtil.buildSql(database, Coffee.NAME.eq("Espresso").xor(Coffee.PRICE.gt(2.0))).getKey());
        assertEquals("(NOT ((\"name\") = (?)))", SQLUtil.buildSql(database, Coffee.NAME.eq("Espresso").not()).getKey());
    }

    @Test
    public void testIsNullAndIsNotNull() {
        assertEquals("(\"name\") IS NULL", SQLUtil.buildSql(database, Coffee.NAME.checkNull()).getKey());
        assertEquals("(\"name\") IS NOT NULL", SQLUtil.buildSql(database, Coffee.NAME.checkNotNull()).getKey());
        assertEquals("(\"name\") IS NULL", SQLUtil.buildSqlWithoutPlaceholders(database, Coffee.NAME.checkNull()));
        assertEquals("(\"name\") IS NOT NULL", SQLUtil.buildSqlWithoutPlaceholders(database, Coffee.NAME.checkNotNull()));
    }

    @Test
    public void testCurrentTimestamp() {
        assertEquals("CURRENT_TIMESTAMP", SQLUtil.buildSql(database, Expression.currentTimestamp()).getKey());
        assertEquals("CURRENT_TIMESTAMP", SQLUtil.buildSqlWithoutPlaceholders(database, Expression.currentTimestamp()));
    }

    @Test
    public void testColumnToColumnComparison() {
        assertEquals("(\"name\") = (\"name\")", SQLUtil.buildSql(database, Coffee.NAME.eq(Coffee.NAME)).getKey());
    }

    @Test
    public void testLogicalOperatorsWithoutPlaceholders() {
        assertEquals("(\"name\") = ('Espresso')", SQLUtil.buildSqlWithoutPlaceholders(database, Coffee.NAME.eq("Espresso")));
        assertEquals("(\"name\") != ('Espresso')", SQLUtil.buildSqlWithoutPlaceholders(database, Coffee.NAME.neq("Espresso")));
        assertEquals("(\"price\") > (2.0)", SQLUtil.buildSqlWithoutPlaceholders(database, Coffee.PRICE.gt(2.0)));
        assertEquals("((\"name\") = ('Espresso')) AND ((\"price\") > (2.0))", SQLUtil.buildSqlWithoutPlaceholders(database, Coffee.NAME.eq("Espresso").and(Coffee.PRICE.gt(2.0))));
        assertEquals("((\"name\") = ('Espresso')) OR ((\"price\") > (2.0))", SQLUtil.buildSqlWithoutPlaceholders(database, Coffee.NAME.eq("Espresso").or(Coffee.PRICE.gt(2.0))));
        assertEquals("((\"name\") = ('Espresso')) XOR ((\"price\") > (2.0))", SQLUtil.buildSqlWithoutPlaceholders(database, Coffee.NAME.eq("Espresso").xor(Coffee.PRICE.gt(2.0))));
        assertEquals("(NOT ((\"name\") = ('Espresso')))", SQLUtil.buildSqlWithoutPlaceholders(database, Coffee.NAME.eq("Espresso").not()));
        Instant instant = Instant.parse("2023-01-01T00:00:00Z");
        assertEquals("'2023-01-01T00:00:00Z'", SQLUtil.buildSqlWithoutPlaceholders(database, Expression.constant(instant)));
    }
}