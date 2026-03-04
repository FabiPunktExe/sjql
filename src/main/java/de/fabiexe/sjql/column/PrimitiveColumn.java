package de.fabiexe.sjql.column;

import de.fabiexe.sjql.Table;
import de.fabiexe.sjql.expression.Expression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract sealed class PrimitiveColumn<T> implements Column<T>
        permits BooleanColumn, DoubleColumn, FloatColumn, IntColumn, LongColumn, StringColumn, UUIDColumn, TimestampColumn {
    protected final Table<?> table;
    protected final String name;
    protected final Class<T> type;
    protected Expression defaultValue = null;
    protected boolean isPrimaryKey = false;
    protected boolean notNull = false;

    public PrimitiveColumn(@NotNull Table<?> table, @NotNull String name, @NotNull Class<T> type) {
        this.table = table;
        this.name = name;
        this.type = type;
    }

    @Override
    public @NotNull Table<?> table() {
        return table;
    }

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public @NotNull Class<T> type() {
        return type;
    }

    @Override
    public @Nullable Expression defaultValue() {
        return defaultValue;
    }

    @Override
    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    @Override
    public boolean isNotNull() {
        return notNull;
    }

    @Override
    public @NotNull PrimitiveColumn<T> defaultValue(@NotNull Expression defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    @Override
    public @NotNull PrimitiveColumn<@NotNull T> primaryKey() {
        isPrimaryKey = true;
        notNull = true;
        return this;
    }

    @Override
    public @NotNull PrimitiveColumn<@NotNull T> notNull() {
        notNull = true;
        return this;
    }

    @SuppressWarnings("unchecked")
    public static <T> @NotNull PrimitiveColumn<T> create(@NotNull Class<T> type, @NotNull Table<?> table, @NotNull String name) {
        return switch (type.getName()) {
            case "int", "java.lang.Integer" -> (PrimitiveColumn<T>) new IntColumn(table, name);
            case "double", "java.lang.Double" -> (PrimitiveColumn<T>) new DoubleColumn(table, name);
            case "long", "java.lang.Long" -> (PrimitiveColumn<T>) new LongColumn(table, name);
            case "float", "java.lang.Float" -> (PrimitiveColumn<T>) new FloatColumn(table, name);
            case "boolean", "java.lang.Boolean" -> (PrimitiveColumn<T>) new BooleanColumn(table, name);
            case "java.util.UUID" -> (PrimitiveColumn<T>) new UUIDColumn(table, name);
            case "java.time.Instant" -> (PrimitiveColumn<T>) new TimestampColumn(table, name);
            default -> throw new IllegalArgumentException("Unsupported type: " + type.getName());
        };
    }
}
