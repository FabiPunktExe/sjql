package de.fabiexe.sjql;

import de.fabiexe.sjql.column.Column;

import java.util.UUID;

public record Item(int id, String name) {
    public static final Table<Item> TABLE = new Table<>("items", Item.class);
    public static final Column<UUID> ID = TABLE.uuidColumn("id").primaryKey();
    public static final Column<String> NAME = TABLE.stringColumn("name", 64);
}
