package de.fabiexe.sjql.query;

import de.fabiexe.sjql.Table;
import de.fabiexe.sjql.UpdateStatement;
import de.fabiexe.sjql.column.Column;
import de.fabiexe.sjql.expression.Expression;
import de.fabiexe.sjql.expression.constant.ConstantExpression;
import de.fabiexe.sjql.row.BasicWritableRow;
import de.fabiexe.sjql.row.WritableRow;
import de.fabiexe.sjql.util.SQLUtil;
import de.fabiexe.sjql.util.ThrowingSupplier;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class BasicUpdateStatement<T> implements UpdateStatement {
    private final Table<T> table;
    private final ThrowingSupplier<Connection, SQLException> connectionSupplier;
    private final WritableRow row;
    private Expression condition = null;

    public BasicUpdateStatement(@NotNull Table<T> table, @NotNull ThrowingSupplier<Connection, SQLException> connectionSupplier, @NotNull Consumer<WritableRow> builder) {
        this.table = table;
        this.connectionSupplier = connectionSupplier;
        this.row = new BasicWritableRow<>(table);
        builder.accept(row);
    }

    @Override
    public @NotNull UpdateStatement where(@NotNull Expression condition) {
        this.condition = condition;
        return this;
    }

    @Override
    public void execute() throws SQLException {
        try (Connection connection = connectionSupplier.get()) {
            Map.Entry<String, List<ConstantExpression<?>>> sql = buildSql();
            List<ConstantExpression<?>> parameters = sql.getValue();
            try (PreparedStatement statement = connection.prepareStatement(sql.getKey())) {
                for (int i = 0; i < parameters.size(); i++) {
                    SQLUtil.setObject(statement, i + 1, parameters.get(i));
                }
                statement.executeUpdate();
            }
        }
    }

    private @NotNull Map.Entry<String, List<ConstantExpression<?>>> buildSql() {
        StringBuilder sql = new StringBuilder("UPDATE ").append(table.getName()).append(" SET ");
        List<ConstantExpression<?>> parameters = new ArrayList<>();

        boolean first = true;
        for (Column<?> column : table.getColumns()) {
            if (!row.contains(column)) {
                continue;
            }
            if (first) {
                first = false;
            } else {
                sql.append(", ");
            }
            sql.append(column.name()).append(" = ?");
            if (row.get(column) instanceof ConstantExpression<?> expression) {
                parameters.add(expression);
            } else {
                throw new IllegalArgumentException("Only constant expressions are supported in update set for now");
            }
        }

        if (condition != null) {
            sql.append(" WHERE ");
            Map.Entry<String, List<ConstantExpression<?>>> conditionSql = SQLUtil.buildSql(condition);
            sql.append(conditionSql.getKey());
            parameters.addAll(conditionSql.getValue());
        }

        return Map.entry(sql.toString(), parameters);
    }
}
