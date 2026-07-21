package de.fabiexe.sjql;

import de.fabiexe.sjql.column.Column;

public record Coffee(String name, double price) {
    public static final Table<Coffee> TABLE = new Table<>(Coffee.class, "coffee");
    public static final Column<String> NAME = TABLE.stringColumn("name", 64).notNull();
    public static final Column<Double> PRICE = TABLE.doubleColumn("price").notNull().defaultValue(4.5);
}
