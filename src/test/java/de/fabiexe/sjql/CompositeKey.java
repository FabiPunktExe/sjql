package de.fabiexe.sjql;

import de.fabiexe.sjql.column.Column;

public record CompositeKey(int id1, int id2, String data) {
    public static final Table<CompositeKey> TABLE = new Table<>(CompositeKey.class, "composite_key");
    public static final Column<Integer> ID1 = TABLE.intColumn("id1").primaryKey();
    public static final Column<Integer> ID2 = TABLE.intColumn("id2").primaryKey();
    public static final Column<String> DATA = TABLE.stringColumn("data", 64);
}
