package de.fabiexe.sjql;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TableTest {
    @Test
    public void test() {
        Table<Coffee> table = new Table<>(Coffee.class, "coffee");

        table.stringColumn("name", 64);
        assertThrows(IllegalArgumentException.class, table::getRowMapper);

        table.doubleColumn("price");
        assertDoesNotThrow(table::getRowMapper);

        table.doubleColumn("temperature");
        assertThrows(IllegalArgumentException.class, table::getRowMapper);
    }
}
