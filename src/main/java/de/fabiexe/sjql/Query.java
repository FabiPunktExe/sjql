package de.fabiexe.sjql;

import de.fabiexe.sjql.column.Column;
import de.fabiexe.sjql.expression.Expression;
import de.fabiexe.sjql.expression.dynamic.ColumnExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a SQL query that can be executed to retrieve results of type {@code T}.
 *
 * @param <T> the type of the result returned by the query
 */
public interface Query<T> {
    /**
     * Adds a {@code WHERE} clause to the query with the specified condition.
     *
     * @param condition The condition to be applied in the {@code WHERE} clause
     * @return A {@link Query} with the added {@code WHERE} clause
     */
    @NotNull Query<T> where(@NotNull Expression condition);

    /**
     * Adds an {@code ORDER BY} clause to the query with the specified expression and sort order.
     *
     * @param expression The expression to sort by
     * @param ascending {@code true} for ascending order, {@code false} for descending order
     * @return A {@link Query} with the added {@code ORDER BY} clause
     */
    @NotNull Query<T> orderBy(@NotNull Expression expression, boolean ascending);

    /**
     * Adds an {@code ORDER BY} clause to the query with multiple expressions and their corresponding sort orders.
     *
     * @param ordering A list of pairs, where each pair consists of an expression and a boolean indicating the sort order
     * @return A {@link Query} with the added {@code ORDER BY} clause
     */
    @NotNull Query<T> orderBy(@NotNull List<Map.Entry<Expression, Boolean>> ordering);

    /**
     * Adds an {@code ORDER BY} clause to the query for the specified column and sort order.
     *
     * @param column The column to sort by
     * @param ascending {@code true} for ascending order, {@code false} for descending order
     * @return A {@link Query} with the added {@code ORDER BY} clause
     */
    default @NotNull Query<T> orderBy(@NotNull Column<?> column, boolean ascending) {
        return orderBy(new ColumnExpression<>(column), ascending);
    }

    /**
     * Adds an {@code ORDER BY} clause to the query for the specified columns and their corresponding sort orders.
     *
     * @param column1 The first column to sort by
     * @param ascending1 {@code true} for ascending order, {@code false} for descending order for the first column
     * @param column2 The second column to sort by
     * @param ascending2 {@code true} for ascending order, {@code false} for descending order for the second column
     * @return A {@link Query} with the added {@code ORDER BY} clause
     */
    default @NotNull Query<T> orderBy(
            @NotNull Column<?> column1, boolean ascending1,
            @NotNull Column<?> column2, boolean ascending2
    ) {
        return orderBy(List.of(
                Map.entry(new ColumnExpression<>(column1), ascending1),
                Map.entry(new ColumnExpression<>(column2), ascending2)
        ));
    }

    /**
     * Adds an {@code ORDER BY} clause to the query for the specified columns and their corresponding sort orders.
     *
     * @param column1 The first column to sort by
     * @param ascending1 {@code true} for ascending order, {@code false} for descending order for the first column
     * @param column2 The second column to sort by
     * @param ascending2 {@code true} for ascending order, {@code false} for descending order for the second column
     * @param column3 The third column to sort by
     * @param ascending3 {@code true} for ascending order, {@code false} for descending order for the third column
     * @return A {@link Query} with the added {@code ORDER BY} clause
     */
    default @NotNull Query<T> orderBy(
            @NotNull Column<?> column1, boolean ascending1,
            @NotNull Column<?> column2, boolean ascending2,
            @NotNull Column<?> column3, boolean ascending3
    ) {
        return orderBy(List.of(
                Map.entry(new ColumnExpression<>(column1), ascending1),
                Map.entry(new ColumnExpression<>(column2), ascending2),
                Map.entry(new ColumnExpression<>(column3), ascending3)
        ));
    }

    /**
     * Adds a {@code LIMIT} clause to the query with the specified limit.
     *
     * @param limit The maximum number of results to return, or {@code null} for no limit
     * @return A {@link Query} with the added {@code LIMIT} clause
     */
    @NotNull Query<T> limit(@Nullable Long limit);

    /**
     * Adds a {@code COUNT} clause to the query to count the number of results.
     *
     * @return A {@link Query} that returns the count of results when executed
     */
    @NotNull Query<Long> count();

    /**
     * Executes the query and returns the result.
     *
     * @return The result of the query, or {@code null} if no results were found
     * @throws SQLException If a database error occurs during query execution
     */
    @Nullable T execute() throws SQLException;

    /**
     * Executes the query and returns the result, ensuring that it is not {@code null}.
     *
     * @return The non-null result of the query
     * @throws SQLException If a database error occurs during query execution
     * @throws NullPointerException If the query result is {@code null}
     */
    default @NotNull T executeNotNull() throws SQLException {
        return Objects.requireNonNull(execute(), "Query result is null");
    }
}
