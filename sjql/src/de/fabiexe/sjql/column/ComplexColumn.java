package de.fabiexe.sjql.column;

import de.fabiexe.sjql.Table;
import de.fabiexe.sjql.expression.Expression;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * A column that maps a complex Java type to a primitive database column.
 *
 * @param <T> the complex Java type
 * @param <B> the primitive base type stored in the database
 */
public non-sealed abstract class ComplexColumn<T extends @Nullable Object, B extends @Nullable Object> implements Column<T> {
    private final Class<T> type;
    private final Column<B> base;

    /**
     * Creates a complex column backed by the given base column.
     *
     * @param type the complex Java type
     * @param base the primitive base column
     */
    public ComplexColumn(Class<T> type, Column<B> base) {
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
            Class<T> type,
            Class<B> primitiveType,
            Table<?> table,
            String name
    ) {
        this(type, PrimitiveColumn.create(primitiveType, table, name));
    }

    /**
     * Converts a primitive database value to the complex type.
     *
     * @param value the primitive value
     * @return the complex value
     */
    public abstract @Nullable T toComplex(@Nullable B value);

    /**
     * Converts a complex value to the primitive database type.
     *
     * @param value the complex value
     * @return the primitive value
     */
    public abstract @Nullable B toBase(@Nullable T value);

    @Override
    public Table<?> table() {
        return base.table();
    }

    @Override
    public String name() {
        return base.name();
    }

    @Override
    public Class<T> type() {
        return type;
    }

    @Override
    public @Nullable Expression defaultValue() {
        return base.defaultValue();
    }

    @Override
    public Column<T> defaultValue(Expression defaultValue) {
        base.defaultValue(defaultValue);
        return this;
    }

    @Override
    public boolean isPrimaryKey() {
        return base.isPrimaryKey();
    }

    @Override
    public Column<@NonNull T> primaryKey() {
        base.primaryKey();
        return this;
    }

    @Override
    public boolean isUnique() {
        return base.isUnique();
    }

    @Override
    public Column<@NonNull T> unique() {
        base.unique();
        return this;
    }

    @Override
    public boolean isNotNull() {
        return base.isNotNull();
    }

    @Override
    public Column<@NonNull T> notNull() {
        base.notNull();
        return this;
    }

    /**
     * Gets the base column of this complex column.
     *
     * @return The base column
     */
    public Column<B> getBase() {
        return base;
    }
}