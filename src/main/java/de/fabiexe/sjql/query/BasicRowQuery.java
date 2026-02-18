package de.fabiexe.sjql.query;

import de.fabiexe.sjql.Table;
import de.fabiexe.sjql.column.Column;
import de.fabiexe.sjql.expression.constant.ConstantExpression;
import de.fabiexe.sjql.row.BasicReadableRow;
import de.fabiexe.sjql.row.ReadableRow;
import de.fabiexe.sjql.util.SQLUtil;
import de.fabiexe.sjql.util.ThrowingSupplier;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasicRowQuery<T> extends BasicQuery<T, List<ReadableRow<T>>> {
    public BasicRowQuery(@NotNull Table<T> table, @NotNull ThrowingSupplier<Connection, SQLException> connectionSupplier) {
        super(table, connectionSupplier);
    }

    @Override
    public @NotNull List<ReadableRow<T>> execute() throws SQLException {
        try (Connection connection = connectionSupplier.get()) {
            Map.Entry<String, List<ConstantExpression<?>>> sql = buildSql();
            List<ConstantExpression<?>> parameters = sql.getValue();
            try (PreparedStatement statement = connection.prepareStatement(sql.getKey())) {
                for (int i = 0; i < parameters.size(); i++) {
                    SQLUtil.setObject(statement, i + 1, parameters.get(i));
                }
                ResultSet resultSet = statement.executeQuery();
                List<ReadableRow<T>> result = new ArrayList<>();
                while (resultSet.next()) {
                    Map<Column<?>, Object> rowContent = new HashMap<>();
                    List<Column<?>> columns = table.getColumns();
                    for (int i = 0; i < columns.size(); i++) {
                        Column<?> column = columns.get(i);
                        rowContent.put(column, SQLUtil.getObject(resultSet, i + 1, column));
                    }
                    result.add(new BasicReadableRow<>(table, rowContent));
                }
                return result;
            }
        }
    }

    private @NotNull Map.Entry<String, List<ConstantExpression<?>>> buildSql() {
        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(table.name());
        List<ConstantExpression<?>> parameters = new ArrayList<>();
        if (condition != null) {
            sql.append(" WHERE ");
            Map.Entry<String, List<ConstantExpression<?>>> conditionSql = SQLUtil.buildSql(condition);
            sql.append(conditionSql.getKey());
            parameters.addAll(conditionSql.getValue());
        }
        if (!ordering.isEmpty()) {
            sql.append(" ORDER BY ");
            for (int i = 0; i < ordering.size(); i++) {
                Map.Entry<Column<?>, Boolean> entry = ordering.get(i);
                sql.append(entry.getKey().name()).append(entry.getValue() ? " ASC" : " DESC");
                if (i < ordering.size() - 1) {
                    sql.append(", ");
                }
            }
        }
        if (limit != null) {
            sql.append(" LIMIT ").append(limit);
        }
        return Map.entry(sql.toString(), parameters);
    }
}
