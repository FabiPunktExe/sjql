package de.fabiexe.sjql.column;

import de.fabiexe.sjql.Table;
import de.fabiexe.sjql.expression.Expression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public non-sealed abstract class ComplexColumn<T, B> implements Column<T> {
    private final Class<T> type;
    private final Column<B> base;

    public ComplexColumn(@NotNull Class<T> type, @NotNull Column<B> base) {
        this.type = type;
        this.base = base;
    }

    public ComplexColumn(
            @NotNull Class<T> type,
            @NotNull Class<B> primitiveType,
            @NotNull Table<?> table,
            @NotNull String name
    ) {
        this(type, PrimitiveColumn.create(primitiveType, table, name));
    }

    public abstract T toComplex(B value);
    public abstract B toBase(T value);

    @Override
    public @NotNull Table<?> table() {
        return base.table();
    }

    @Override
    public @NotNull String name() {
        return base.name();
    }

    @Override
    public @NotNull Class<T> type() {
        return type;
    }

    @Override
    public @Nullable Expression defaultValue() {
        return base.defaultValue();
    }

    @Override
    public @NotNull Column<T> defaultValue(@NotNull Expression defaultValue) {
        base.defaultValue(defaultValue);
        return this;
    }

    @Override
    public boolean isPrimaryKey() {
        return base.isPrimaryKey();
    }

    @Override
    public @NotNull Column<T> primaryKey() {
        base.primaryKey();
        return this;
    }

    @Override
    public boolean isNotNull() {
        return base.isNotNull();
    }

    @Override
    public @NotNull Column<T> notNull() {
        base.notNull();
        return this;
    }

    /**
     * Gets the base column of this complex column.
     *
     * @return The base column
     */
    public @NotNull Column<B> getBase() {
        return base;
    }
}
