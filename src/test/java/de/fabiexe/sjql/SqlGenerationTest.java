package de.fabiexe.sjql;

import de.fabiexe.sjql.util.SQLUtil;
import org.junit.jupiter.api.Test;

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
    }

    @Test
    public void testColumnToColumnComparison() {
        assertEquals("(name) = (name)", SQLUtil.buildSql(Coffee.NAME.eq(Coffee.NAME)).getKey());
    }
}
