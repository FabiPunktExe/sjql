package de.fabiexe.sjql;

import de.fabiexe.sjql.expression.Expression;
import de.fabiexe.sjql.util.SQLUtil;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SqlGenerationTest {
    @Test
    public void testLogicalOperators() {
        assertEquals("(name) = (?)", SQLUtil.buildSql(Coffee.NAME.eq("Espresso")).getKey());
        assertEquals("(name) != (?)", SQLUtil.buildSql(Coffee.NAME.ne("Espresso")).getKey());
        assertEquals("(price) > (?)", SQLUtil.buildSql(Coffee.PRICE.gt(2.0)).getKey());
        assertEquals("(price) >= (?)", SQLUtil.buildSql(Coffee.PRICE.gte(2.5)).getKey());
        assertEquals("(price) < (?)", SQLUtil.buildSql(Coffee.PRICE.lt(3.0)).getKey());
        assertEquals("(price) <= (?)", SQLUtil.buildSql(Coffee.PRICE.lte(2.5)).getKey());
        assertEquals("((name) = (?)) AND ((price) > (?))", SQLUtil.buildSql(Coffee.NAME.eq("Espresso").and(Coffee.PRICE.gt(2.0))).getKey());
        assertEquals("((name) = (?)) OR ((price) > (?))", SQLUtil.buildSql(Coffee.NAME.eq("Espresso").or(Coffee.PRICE.gt(2.0))).getKey());
        assertEquals("((name) = (?)) XOR ((price) > (?))", SQLUtil.buildSql(Coffee.NAME.eq("Espresso").xor(Coffee.PRICE.gt(2.0))).getKey());
        assertEquals("(NOT ((name) = (?)))", SQLUtil.buildSql(Coffee.NAME.eq("Espresso").not()).getKey());
    }

    @Test
    public void testCurrentTimestamp() {
        assertEquals("CURRENT_TIMESTAMP", SQLUtil.buildSql(Expression.currentTimestamp()).getKey());
        assertEquals("CURRENT_TIMESTAMP", SQLUtil.buildSqlWithoutPlaceholders(Expression.currentTimestamp()));
    }

    @Test
    public void testColumnToColumnComparison() {
        assertEquals("(name) = (name)", SQLUtil.buildSql(Coffee.NAME.eq(Coffee.NAME)).getKey());
    }

    @Test
    public void testLogicalOperatorsWithoutPlaceholders() {
        assertEquals("(name) = ('Espresso')", SQLUtil.buildSqlWithoutPlaceholders(Coffee.NAME.eq("Espresso")));
        assertEquals("(name) != ('Espresso')", SQLUtil.buildSqlWithoutPlaceholders(Coffee.NAME.ne("Espresso")));
        assertEquals("(price) > (2.0)", SQLUtil.buildSqlWithoutPlaceholders(Coffee.PRICE.gt(2.0)));
        assertEquals("((name) = ('Espresso')) AND ((price) > (2.0))", SQLUtil.buildSqlWithoutPlaceholders(Coffee.NAME.eq("Espresso").and(Coffee.PRICE.gt(2.0))));
        assertEquals("((name) = ('Espresso')) OR ((price) > (2.0))", SQLUtil.buildSqlWithoutPlaceholders(Coffee.NAME.eq("Espresso").or(Coffee.PRICE.gt(2.0))));
        assertEquals("((name) = ('Espresso')) XOR ((price) > (2.0))", SQLUtil.buildSqlWithoutPlaceholders(Coffee.NAME.eq("Espresso").xor(Coffee.PRICE.gt(2.0))));
        assertEquals("(NOT ((name) = ('Espresso')))", SQLUtil.buildSqlWithoutPlaceholders(Coffee.NAME.eq("Espresso").not()));
        Instant instant = Instant.parse("2023-01-01T00:00:00Z");
        assertEquals("'2023-01-01T00:00:00Z'", SQLUtil.buildSqlWithoutPlaceholders(Expression.constant(instant)));
    }
}
