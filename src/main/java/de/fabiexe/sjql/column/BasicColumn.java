package de.fabiexe.sjql.column;

import de.fabiexe.sjql.Table;
import de.fabiexe.sjql.expression.Expression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract sealed class BasicColumn<T> implements Column<T>
        permits BooleanColumn, DoubleColumn, FloatColumn, IntColumn, LongColumn, StringColumn, UUIDColumn {
    protected final Table<?> table;
    protected final String name;
    protected final Class<T> type;
    protected Expression defaultValue = null;

    public BasicColumn(
            @NotNull Table<?> table,
            @NotNull String name,
            @NotNull Class<T> type
    ) {
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
        return null;
    }

    @Override
    public @NotNull BasicColumn<T> defaultValue(@NotNull Expression defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }
}
