package de.fabiexe.sjql.column;

import de.fabiexe.sjql.TableRef;
import de.fabiexe.sjql.expression.Expression;
import de.fabiexe.sjql.expression.dynamic.ColumnExpression;
import org.jspecify.annotations.Nullable;

/**
 * A reference to a {@link Column}.
 *
 * @param <T> The type of the column
 */
public interface ColumnRef<T extends @Nullable Object> extends Expression {
    /**
     * Gets the table reference this column belongs to.
     *
     * @return The table reference
     */
    TableRef<?> getTableRef();

    /**
     * Gets the name of the referenced column.
     *
     * @return The column name
     */
    String name();

    /**
     * Creates an {@code =} comparison between this column reference and another column.
     *
     * @param other The column to compare with
     * @return A new {@link Expression}
     */
    default Expression eq(Column<?> other) {
        return eq(new ColumnExpression<>(other));
    }

    /**
     * Creates a {@code !=} comparison between this column reference and another column.
     *
     * @param other The column to compare with
     * @return A new {@link Expression}
     */
    default Expression neq(Column<?> other) {
        return neq(new ColumnExpression<>(other));
    }

    /**
     * Creates a {@code >} comparison between this column reference and another column.
     *
     * @param other The column to compare with
     * @return A new {@link Expression}
     */
    default Expression gt(Column<?> other) {
        return gt(new ColumnExpression<>(other));
    }

    /**
     * Creates a {@code >=} comparison between this column reference and another column.
     *
     * @param other The column to compare with
     * @return A new {@link Expression}
     */
    default Expression gte(Column<?> other) {
        return gte(new ColumnExpression<>(other));
    }

    /**
     * Creates a {@code <} comparison between this column reference and another column.
     *
     * @param other The column to compare with
     * @return A new {@link Expression}
     */
    default Expression lt(Column<?> other) {
        return lt(new ColumnExpression<>(other));
    }

    /**
     * Creates a {@code <=} comparison between this column reference and another column.
     *
     * @param other The column to compare with
     * @return A new {@link Expression}
     */
    default Expression lte(Column<?> other) {
        return lte(new ColumnExpression<>(other));
    }
}