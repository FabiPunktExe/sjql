package de.fabiexe.sjql;

import de.fabiexe.sjql.column.Column;
import de.fabiexe.sjql.column.ColumnRef;
import de.fabiexe.sjql.row.ReadableRow;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * A reference to a {@link Table}, optionally qualified by an alias.
 *
 * @param <T> The type of the objects that represent rows in the referenced table
 */
public interface TableRef<T extends @Nullable Object> {
    /**
     * Gets the table that this reference points to.
     *
     * @return The referenced table
     */
    Table<T> getTable();

    /**
     * Gets the alias of this reference, or {@code null} if no alias is set.
     *
     * @return The alias, or {@code null} if none is set
     */
    @Nullable String getAlias();

    /**
     * Gets the name used to qualify columns of this reference in SQL.
     *
     * @return The alias if one is set, otherwise the table name
     */
    String getQualifier();

    /**
     * Creates a reference to the given column of this table reference.
     *
     * @param <U> The type of the column
     * @param column The column to reference
     * @return A {@link ColumnRef} for the given column
     * @throws IllegalArgumentException Tf the column does not belong to the referenced table
     */
    <U extends @Nullable Object> ColumnRef<U> column(Column<U> column);

    /**
     * Combines this table and the specified tables and creates a new {@link Query} for selecting rows from combination.
     * The tables are listed comma-separated in the {@code FROM} clause.
     * This method may only be used inside a transaction.
     *
     * @param others The other table references to include in the query
     * @return A {@link Query} that returns the combined rows
     * @throws IllegalStateException If this method is not called inside a transaction
     * @throws IllegalArgumentException If a duplicate alias or table reference is used
     * @see Database#transaction(Runnable)
     */
    Query<List<ReadableRow>> combine(TableRef<?>... others);
}