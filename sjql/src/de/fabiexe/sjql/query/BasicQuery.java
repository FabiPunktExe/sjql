package de.fabiexe.sjql.query;

import de.fabiexe.sjql.Query;
import de.fabiexe.sjql.TableRef;
import de.fabiexe.sjql.database.BasicDatabase;
import de.fabiexe.sjql.expression.Expression;
import de.fabiexe.sjql.util.ThrowingSupplier;
import org.jspecify.annotations.Nullable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Base implementation of {@link Query} that stores the tables, connection supplier and query modifiers.
 *
 * @param <T> The type of the query result
 */
public abstract class BasicQuery<T extends @Nullable Object> implements Query<T> {
    /** The database this query operates on. */
    protected final BasicDatabase database;

    /** The tables this query operates on. */
    protected final List<TableRef<?>> tables;

    /** Supplier for the database connection used to execute the query. */
    protected final ThrowingSupplier<Connection, SQLException> connectionSupplier;

    /** The optional {@code WHERE} condition, or {@code null} if none was set. */
    protected @Nullable Expression condition = null;

    /** The {@code ORDER BY} expressions together with their sort direction. */
    protected List<Map.Entry<Expression, Boolean>> ordering = new ArrayList<>();

    /** The optional {@code LIMIT}, or {@code null} if none was set. */
    protected @Nullable Long limit = null;

    /**
     * Creates a new basic query for the given tables.
     *
     * @param database The database to query
     * @param tables The tables to query
     * @param connectionSupplier supplier for the database connection
     * @throws IllegalArgumentException If no table is given or two tables share the same qualifier
     */
    public BasicQuery(
            BasicDatabase database,
            List<TableRef<?>> tables,
            ThrowingSupplier<Connection, SQLException> connectionSupplier
    ) {
        this.database = database;
        this.tables = tables;
        this.connectionSupplier = connectionSupplier;
        validateTables();
    }

    private void validateTables() {
        if (tables.isEmpty()) {
            throw new IllegalArgumentException("At least one table must be specified");
        }

        Set<String> qualifiers = new HashSet<>();
        for (TableRef<?> table : tables) {
            if (qualifiers.contains(table.getQualifier())) {
                throw new IllegalArgumentException("Duplicate table reference '" + table.getQualifier() + "'; use Table.alias(...) to disambiguate");
            } else {
                qualifiers.add(table.getQualifier());
            }
        }
    }

    @Override
    public Query<T> where(@Nullable Expression condition) {
        this.condition = condition;
        return this;
    }

    @Override
    public Query<T> orderBy(Expression expression, boolean ascending) {
        this.ordering.add(Map.entry(expression, ascending));
        return this;
    }

    @Override
    public Query<T> orderBy(List<Map.Entry<Expression, Boolean>> ordering) {
        this.ordering.addAll(ordering);
        return this;
    }

    @Override
    public Query<T> limit(@Nullable Long limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public Query<Long> count() {
        return new BasicCountQuery(database, this);
    }
}