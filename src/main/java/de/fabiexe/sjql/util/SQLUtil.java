package de.fabiexe.sjql.util;

import de.fabiexe.sjql.column.Column;
import de.fabiexe.sjql.column.DoubleColumn;
import de.fabiexe.sjql.column.IntColumn;
import de.fabiexe.sjql.column.StringColumn;
import de.fabiexe.sjql.expression.Expression;
import de.fabiexe.sjql.expression.constant.ConstantExpression;
import de.fabiexe.sjql.expression.constant.DoubleExpression;
import de.fabiexe.sjql.expression.constant.IntExpression;
import de.fabiexe.sjql.expression.constant.StringExpression;
import de.fabiexe.sjql.expression.logical.EqualsExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SQLUtil {
    public static void setObject(@NotNull PreparedStatement statement, int index, @NotNull ConstantExpression<?> expression) throws SQLException {
        switch (expression) {
            case IntExpression(Integer value) -> statement.setObject(index, value, Types.INTEGER);
            case DoubleExpression(Double value) -> statement.setObject(index, value, Types.DOUBLE);
            case StringExpression(String value) -> statement.setObject(index, value, Types.VARCHAR);
        }
    }

    public static <T> @Nullable T getObject(@NotNull ResultSet resultSet, int index, @NotNull Column<T> column) throws SQLException {
        return (T) switch (column) {
            case IntColumn _ -> resultSet.getObject(index, Integer.class);
            case DoubleColumn _ -> resultSet.getObject(index, Double.class);
            case StringColumn _ -> resultSet.getObject(index, String.class);
        };
    }

    public static @NotNull Map.Entry<String, List<ConstantExpression<?>>> buildSql(@NotNull Expression expression) {
        return switch (expression) {
            case ConstantExpression<?> constantExpression -> Map.entry("?", List.of(constantExpression));
            case EqualsExpression(Expression a, Expression b) -> {
                Map.Entry<String, List<ConstantExpression<?>>> aSql = buildSql(a);
                Map.Entry<String, List<ConstantExpression<?>>> bSql = buildSql(b);
                String sql = "(" + aSql.getKey() + ") = (" + bSql.getKey() + ")";
                List<ConstantExpression<?>> parameters = new ArrayList<>();
                parameters.addAll(aSql.getValue());
                parameters.addAll(bSql.getValue());
                yield Map.entry(sql, parameters);
            }
            default -> throw new IllegalArgumentException("Unsupported expression type: " + expression.getClass().getName());
        };
    }
}
