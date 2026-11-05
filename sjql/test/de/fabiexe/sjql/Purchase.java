package de.fabiexe.sjql;

import de.fabiexe.sjql.column.Column;

public record Purchase(int id, String coffeeName, int amount) {
    public static final Table<Purchase> TABLE = new Table<>(Purchase.class, "purchase");
    public static final Column<Integer> ID = TABLE.intColumn("id").notNull();
    public static final Column<String> COFFEE_NAME = TABLE.stringColumn("coffee_name", 64).notNull();
    public static final Column<Integer> AMOUNT = TABLE.intColumn("amount").notNull();
}