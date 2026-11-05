package de.fabiexe.sjql;

import de.fabiexe.sjql.database.H2Database;
import de.fabiexe.sjql.expression.Expression;
import de.fabiexe.sjql.util.SQLUtil;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

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
        assertEquals("(\"coffee\".\"name\") = (?)", SQLUtil.buildSql(database, Coffee.NAME.eq("Espresso")).getKey());
        assertEquals("(\"coffee\".\"name\") != (?)", SQLUtil.buildSql(database, Coffee.NAME.neq("Espresso")).getKey());
        assertEquals("(\"coffee\".\"price\") > (?)", SQLUtil.buildSql(database, Coffee.PRICE.gt(2.0)).getKey());
        assertEquals("(\"coffee\".\"price\") >= (?)", SQLUtil.buildSql(database, Coffee.PRICE.gte(2.5)).getKey());
        assertEquals("(\"coffee\".\"price\") < (?)", SQLUtil.buildSql(database, Coffee.PRICE.lt(3.0)).getKey());
        assertEquals("(\"coffee\".\"price\") <= (?)", SQLUtil.buildSql(database, Coffee.PRICE.lte(2.5)).getKey());
        assertEquals("((\"coffee\".\"name\") = (?)) AND ((\"coffee\".\"price\") > (?))", SQLUtil.buildSql(database, Coffee.NAME.eq("Espresso").and(Coffee.PRICE.gt(2.0))).getKey());
        assertEquals("((\"coffee\".\"name\") = (?)) OR ((\"coffee\".\"price\") > (?))", SQLUtil.buildSql(database, Coffee.NAME.eq("Espresso").or(Coffee.PRICE.gt(2.0))).getKey());
        assertEquals("((\"coffee\".\"name\") = (?)) XOR ((\"coffee\".\"price\") > (?))", SQLUtil.buildSql(database, Coffee.NAME.eq("Espresso").xor(Coffee.PRICE.gt(2.0))).getKey());
        assertEquals("(NOT ((\"coffee\".\"name\") = (?)))", SQLUtil.buildSql(database, Coffee.NAME.eq("Espresso").not()).getKey());
    }

    @Test
    public void testIsNullAndIsNotNull() {
        assertEquals("(\"coffee\".\"name\") IS NULL", SQLUtil.buildSql(database, Coffee.NAME.checkNull()).getKey());
        assertEquals("(\"coffee\".\"name\") IS NOT NULL", SQLUtil.buildSql(database, Coffee.NAME.checkNotNull()).getKey());
        assertEquals("(\"coffee\".\"name\") IS NULL", SQLUtil.buildSqlWithoutPlaceholders(database, Coffee.NAME.checkNull()));
        assertEquals("(\"coffee\".\"name\") IS NOT NULL", SQLUtil.buildSqlWithoutPlaceholders(database, Coffee.NAME.checkNotNull()));
    }

    @Test
    public void testCurrentTimestamp() {
        assertEquals("CURRENT_TIMESTAMP", SQLUtil.buildSql(database, Expression.currentTimestamp()).getKey());
        assertEquals("CURRENT_TIMESTAMP", SQLUtil.buildSqlWithoutPlaceholders(database, Expression.currentTimestamp()));
    }

    @Test
    public void testColumnToColumnComparison() {
        assertEquals("(\"coffee\".\"name\") = (\"coffee\".\"name\")", SQLUtil.buildSql(database, Coffee.NAME.eq(Coffee.NAME)).getKey());
    }

    @Test
    public void testLogicalOperatorsWithoutPlaceholders() {
        assertEquals("(\"coffee\".\"name\") = ('Espresso')", SQLUtil.buildSqlWithoutPlaceholders(database, Coffee.NAME.eq("Espresso")));
        assertEquals("(\"coffee\".\"name\") != ('Espresso')", SQLUtil.buildSqlWithoutPlaceholders(database, Coffee.NAME.neq("Espresso")));
        assertEquals("(\"coffee\".\"price\") > (2.0)", SQLUtil.buildSqlWithoutPlaceholders(database, Coffee.PRICE.gt(2.0)));
        assertEquals("((\"coffee\".\"name\") = ('Espresso')) AND ((\"coffee\".\"price\") > (2.0))", SQLUtil.buildSqlWithoutPlaceholders(database, Coffee.NAME.eq("Espresso").and(Coffee.PRICE.gt(2.0))));
        assertEquals("((\"coffee\".\"name\") = ('Espresso')) OR ((\"coffee\".\"price\") > (2.0))", SQLUtil.buildSqlWithoutPlaceholders(database, Coffee.NAME.eq("Espresso").or(Coffee.PRICE.gt(2.0))));
        assertEquals("((\"coffee\".\"name\") = ('Espresso')) XOR ((\"coffee\".\"price\") > (2.0))", SQLUtil.buildSqlWithoutPlaceholders(database, Coffee.NAME.eq("Espresso").xor(Coffee.PRICE.gt(2.0))));
        assertEquals("(NOT ((\"coffee\".\"name\") = ('Espresso')))", SQLUtil.buildSqlWithoutPlaceholders(database, Coffee.NAME.eq("Espresso").not()));
        Instant instant = Instant.parse("2023-01-01T00:00:00Z");
        assertEquals("'2023-01-01T00:00:00Z'", SQLUtil.buildSqlWithoutPlaceholders(database, Expression.constant(instant)));
    }

    @Test
    public void testFromClause() {
        assertEquals("\"coffee\"", SQLUtil.buildFromClause(database, List.of(Coffee.TABLE.ref())));
        assertEquals("\"coffee\" AS \"c\"", SQLUtil.buildFromClause(database, List.of(Coffee.TABLE.alias("c"))));
        assertEquals(
                "\"coffee\" AS \"c\", \"purchase\" AS \"p\"",
                SQLUtil.buildFromClause(database, List.of(Coffee.TABLE.alias("c"), Purchase.TABLE.alias("p")))
        );
    }

    @Test
    public void testColumnRef() {
        TableRef<Coffee> coffee = Coffee.TABLE.alias("c");
        TableRef<Purchase> purchase = Purchase.TABLE.alias("p");

        assertEquals("\"c\".\"name\"", SQLUtil.buildSql(database, coffee.column(Coffee.NAME)).getKey());
        assertEquals("(\"c\".\"name\") = (?)", SQLUtil.buildSql(database, coffee.column(Coffee.NAME).eq("Espresso")).getKey());
        assertEquals(
                "(\"c\".\"name\") = (\"p\".\"coffee_name\")",
                SQLUtil.buildSql(database, coffee.column(Coffee.NAME).eq(purchase.column(Purchase.COFFEE_NAME))).getKey()
        );
        assertEquals(
                "(\"c\".\"name\") = (\"coffee\".\"name\")",
                SQLUtil.buildSql(database, coffee.column(Coffee.NAME).eq(Coffee.NAME)).getKey()
        );
        assertEquals("\"c\".\"name\"", SQLUtil.buildSqlWithoutPlaceholders(database, coffee.column(Coffee.NAME)));
    }
}