package de.fabiexe.sjql.query;

import de.fabiexe.sjql.DeleteStatement;
import de.fabiexe.sjql.Table;
import de.fabiexe.sjql.expression.Expression;
import de.fabiexe.sjql.expression.constant.ConstantExpression;
import de.fabiexe.sjql.util.SQLUtil;
import de.fabiexe.sjql.util.ThrowingSupplier;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BasicDeleteStatement<T> implements DeleteStatement {
    private final Table<T> table;
    private final ThrowingSupplier<Connection, SQLException> connectionSupplier;
    private Expression condition = null;

    public BasicDeleteStatement(@NotNull Table<T> table, @NotNull ThrowingSupplier<Connection, SQLException> connectionSupplier) {
        this.table = table;
        this.connectionSupplier = connectionSupplier;
    }

    @Override
    public @NotNull DeleteStatement where(@NotNull Expression condition) {
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
        StringBuilder sql = new StringBuilder("DELETE FROM ").append(table.name());
        List<ConstantExpression<?>> parameters = new ArrayList<>();
        if (condition != null) {
            sql.append(" WHERE ");
            Map.Entry<String, List<ConstantExpression<?>>> conditionSql = SQLUtil.buildSql(condition);
            sql.append(conditionSql.getKey());
            parameters.addAll(conditionSql.getValue());
        }
        return Map.entry(sql.toString(), parameters);
    }
}
