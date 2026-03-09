package de.fabiexe.sjql.util;

import de.fabiexe.sjql.column.*;
import de.fabiexe.sjql.expression.Expression;
import de.fabiexe.sjql.expression.constant.*;
import de.fabiexe.sjql.expression.dynamic.ColumnExpression;
import de.fabiexe.sjql.expression.dynamic.CurrentTimestampExpression;
import de.fabiexe.sjql.expression.logical.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SQLUtil {
    public static void setObject(@NotNull PreparedStatement statement, int index, @NotNull ConstantExpression<?> expression) throws SQLException {
        switch (expression) {
            case IntExpression(Integer value) -> statement.setObject(index, value, Types.INTEGER);
            case DoubleExpression(Double value) -> statement.setObject(index, value, Types.DOUBLE);
            case StringExpression(String value) -> statement.setObject(index, value, Types.VARCHAR);
            case LongExpression(Long value) -> statement.setObject(index, value, Types.BIGINT);
            case FloatExpression(Float value) -> statement.setObject(index, value, Types.FLOAT);
            case BooleanExpression(Boolean value) -> statement.setObject(index, value, Types.BOOLEAN);
            case UUIDExpression(UUID value) -> statement.setObject(index, value, Types.OTHER);
            case TimestampExpression(Instant value) -> statement.setObject(index, Timestamp.from(value), Types.TIMESTAMP);
            case NullExpression _ -> statement.setNull(index, Types.NULL);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> @Nullable T getObject(@NotNull ResultSet resultSet, int index, @NotNull Column<T> column) throws SQLException {
        return (T) switch (column) {
            case IntColumn _ -> resultSet.getObject(index, Integer.class);
            case DoubleColumn _ -> resultSet.getObject(index, Double.class);
            case StringColumn _ -> resultSet.getObject(index, String.class);
            case LongColumn _ -> resultSet.getObject(index, Long.class);
            case FloatColumn _ -> resultSet.getObject(index, Float.class);
            case BooleanColumn _ -> resultSet.getObject(index, Boolean.class);
            case UUIDColumn _ -> resultSet.getObject(index, UUID.class);
            case TimestampColumn _ -> {
                Timestamp timestamp = resultSet.getObject(index, Timestamp.class);
                yield timestamp == null ? null : timestamp.toInstant();
            }
            case ComplexColumn<?, ?> complexColumn -> {
                ComplexColumn<?, Object> complexColumn2 = (ComplexColumn<?, Object>) column;
                Column<Object> baseColumn = (Column<Object>) complexColumn.getBase();
                yield complexColumn2.toComplex(SQLUtil.getObject(resultSet, index, baseColumn));
            }
        };
    }

    public static @NotNull Map.Entry<String, List<ConstantExpression<?>>> buildSql(@NotNull Expression expression) {
        return switch (expression) {
            case ConstantExpression<?> constantExpression -> Map.entry("?", List.of(constantExpression));
            case ColumnExpression<?> columnExpression -> Map.entry(columnExpression.column().name(), List.of());
            case CurrentTimestampExpression _ -> Map.entry("CURRENT_TIMESTAMP", List.of());
            case NotExpression notExpression -> {
                Map.Entry<String, List<ConstantExpression<?>>> sql = buildSql(notExpression.expression());
                yield Map.entry("(NOT (" + sql.getKey() + "))", sql.getValue());
            }
            case LogicalExpression logicalExpression -> {
                Expression a, b;
                String operator;
                switch (logicalExpression) {
                    case EqualsExpression(Expression left, Expression right) -> {
                        a = left;
                        b = right;
                        operator = "=";
                    }
                    case GreaterThanExpression(Expression left, Expression right) -> {
                        a = left;
                        b = right;
                        operator = ">";
                    }
                    case GreaterThanOrEqualExpression(Expression left, Expression right) -> {
                        a = left;
                        b = right;
                        operator = ">=";
                    }
                    case LessThanExpression(Expression left, Expression right) -> {
                        a = left;
                        b = right;
                        operator = "<";
                    }
                    case LessThanOrEqualExpression(Expression left, Expression right) -> {
                        a = left;
                        b = right;
                        operator = "<=";
                    }
                    case NotEqualsExpression(Expression left, Expression right) -> {
                        a = left;
                        b = right;
                        operator = "!=";
                    }
                    case AndExpression(Expression left, Expression right) -> {
                        a = left;
                        b = right;
                        operator = "AND";
                    }
                    case OrExpression(Expression left, Expression right) -> {
                        a = left;
                        b = right;
                        operator = "OR";
                    }
                    case XorExpression(Expression left, Expression right) -> {
                        a = left;
                        b = right;
                        operator = "XOR";
                    }
                    default -> throw new IllegalArgumentException("Unsupported logical expression type: " + expression.getClass().getName());
                }
                Map.Entry<String, List<ConstantExpression<?>>> aSql = buildSql(a);
                Map.Entry<String, List<ConstantExpression<?>>> bSql = buildSql(b);
                String sql = "(" + aSql.getKey() + ") " + operator + " (" + bSql.getKey() + ")";
                List<ConstantExpression<?>> parameters = new ArrayList<>();
                parameters.addAll(aSql.getValue());
                parameters.addAll(bSql.getValue());
                yield Map.entry(sql, parameters);
            }
            case IsNullExpression(Expression inner) -> {
                Map.Entry<String, List<ConstantExpression<?>>> sql = buildSql(inner);
                yield Map.entry("(" + sql.getKey() + ") IS NULL", sql.getValue());
            }
            case IsNotNullExpression(Expression inner) -> {
                Map.Entry<String, List<ConstantExpression<?>>> sql = buildSql(inner);
                yield Map.entry("(" + sql.getKey() + ") IS NOT NULL", sql.getValue());
            }
            default -> throw new IllegalArgumentException("Unsupported expression type: " + expression.getClass().getName());
        };
    }

    public static @NotNull String buildSqlWithoutPlaceholders(@NotNull Expression expression) {
        return switch (expression) {
            case StringExpression stringExpression -> "'" + stringExpression.value().replaceAll("'", "''") + "'";
            case UUIDExpression uuidExpression -> "'" + uuidExpression.value() + "'";
            case TimestampExpression timestampExpression -> "'" + timestampExpression.value() + "'";
            case NullExpression _ -> "NULL";
            case ConstantExpression<?> constantExpression -> String.valueOf(constantExpression.value());
            case ColumnExpression<?> columnExpression -> columnExpression.column().name();
            case CurrentTimestampExpression _ -> "CURRENT_TIMESTAMP";
            case NotExpression notExpression -> {
                String sql = buildSqlWithoutPlaceholders(notExpression.expression());
                yield "(NOT (" + sql + "))";
            }
            case LogicalExpression logicalExpression -> {
                Expression a, b;
                String operator;
                switch (logicalExpression) {
                    case EqualsExpression(Expression left, Expression right) -> {
                        a = left;
                        b = right;
                        operator = "=";
                    }
                    case GreaterThanExpression(Expression left, Expression right) -> {
                        a = left;
                        b = right;
                        operator = ">";
                    }
                    case GreaterThanOrEqualExpression(Expression left, Expression right) -> {
                        a = left;
                        b = right;
                        operator = ">=";
                    }
                    case LessThanExpression(Expression left, Expression right) -> {
                        a = left;
                        b = right;
                        operator = "<";
                    }
                    case LessThanOrEqualExpression(Expression left, Expression right) -> {
                        a = left;
                        b = right;
                        operator = "<=";
                    }
                    case NotEqualsExpression(Expression left, Expression right) -> {
                        a = left;
                        b = right;
                        operator = "!=";
                    }
                    case AndExpression(Expression left, Expression right) -> {
                        a = left;
                        b = right;
                        operator = "AND";
                    }
                    case OrExpression(Expression left, Expression right) -> {
                        a = left;
                        b = right;
                        operator = "OR";
                    }
                    case XorExpression(Expression left, Expression right) -> {
                        a = left;
                        b = right;
                        operator = "XOR";
                    }
                    default -> throw new IllegalArgumentException("Unsupported logical expression type: " + expression.getClass().getName());
                }
                String aSql = buildSqlWithoutPlaceholders(a);
                String bSql = buildSqlWithoutPlaceholders(b);
                yield "(" + aSql + ") " + operator + " (" + bSql + ")";
            }
            case IsNullExpression(Expression inner) -> "(" + buildSqlWithoutPlaceholders(inner) + ") IS NULL";
            case IsNotNullExpression(Expression inner) -> "(" + buildSqlWithoutPlaceholders(inner) + ") IS NOT NULL";
            default -> throw new IllegalArgumentException("Unsupported expression type: " + expression.getClass().getName());
        };
    }
}
