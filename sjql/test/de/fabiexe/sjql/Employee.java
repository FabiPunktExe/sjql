package de.fabiexe.sjql;

import de.fabiexe.sjql.column.Column;
import org.jspecify.annotations.Nullable;

public record Employee(int id, String name, @Nullable Integer managerId) {
    public static final Table<Employee> TABLE = new Table<>(Employee.class, "employee");
    public static final Column<Integer> ID = TABLE.intColumn("id").notNull();
    public static final Column<String> NAME = TABLE.stringColumn("name", 64).notNull();
    public static final Column<@Nullable Integer> MANAGER_ID = TABLE.intColumn("manager_id");
}