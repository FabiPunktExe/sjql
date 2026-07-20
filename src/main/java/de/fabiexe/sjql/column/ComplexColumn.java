package de.fabiexe.sjql.column;

import de.fabiexe.sjql.Table;
import de.fabiexe.sjql.expression.Expression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A column that maps a complex Java type to a primitive database column.
 *
 * @param <T> the complex Java type
 * @param <B> the primitive base type stored in the database
 */
public non-sealed abstract class ComplexColumn<T, B> implements Column<T> {
    private final Class<T> type;
    private final Column<B> base;

    /**
     * Creates a complex column backed by the given base column.
     *
     * @param type the complex Java type
     * @param base the primitive base column
     */
    public ComplexColumn(@NotNull Class<T> type, @NotNull Column<B> base) {
        this.type = type;
        this.base = base;
    }

    /**
     * Creates a complex column backed by a newly created primitive column.
     *
     * @param type the complex Java type
     * @param primitiveType the primitive database type
     * @param table the table to add the column to
     * @param name the column name
     */
    public ComplexColumn(
            @NotNull Class<T> type,
            @NotNull Class<B> primitiveType,
            @NotNull Table<?> table,
            @NotNull String name
    ) {
        this(type, PrimitiveColumn.create(primitiveType, table, name));
    }

    /**
     * Converts a primitive database value to the complex type.
     *
     * @param value the primitive value
     * @return the complex value
     */
    public abstract T toComplex(B value);

    /**
     * Converts a complex value to the primitive database type.
     *
     * @param value the complex value
     * @return the primitive value
     */
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