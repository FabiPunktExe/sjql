package de.fabiexe.sjql;

import de.fabiexe.sjql.column.Column;
import org.jetbrains.annotations.NotNull;

public record Coffee(
        @NotNull String name,
        double price
) {
    public static final Table<Coffee> TABLE = new Table<>("coffee", Coffee.class);
    public static final Column<String> NAME = TABLE.stringColumn("name", 64);
    public static final Column<Double> PRICE = TABLE.doubleColumn("price")
            .defaultValue(4.5);
}
