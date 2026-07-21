package de.fabiexe.sjql.query;

import de.fabiexe.sjql.DeleteStatement;
import de.fabiexe.sjql.Table;
import de.fabiexe.sjql.expression.Expression;
import de.fabiexe.sjql.expression.constant.ConstantExpression;
import de.fabiexe.sjql.util.SQLUtil;
import de.fabiexe.sjql.util.ThrowingSupplier;
import org.jspecify.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A SQL {@code DELETE} statement implementation.
 *
 * @param <T> the type of the objects that represent rows in the target table
 */
public class BasicDeleteStatement<T extends @Nullable Object> implements DeleteStatement {
    private final Table<T> table;
    private final ThrowingSupplier<Connection, SQLException> connectionSupplier;
    private @Nullable Expression condition = null;

    /**
     * Creates a new delete statement for the given table.
     *
     * @param table the table to delete rows from
     * @param connectionSupplier supplier for the database connection
     */
    public BasicDeleteStatement(Table<T> table, ThrowingSupplier<Connection, SQLException> connectionSupplier) {
        this.table = table;
        this.connectionSupplier = connectionSupplier;
    }

    @Override
    public DeleteStatement where(Expression condition) {
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

    private Map.Entry<String, List<ConstantExpression<?>>> buildSql() {
        StringBuilder sql = new StringBuilder("DELETE FROM ").append(table.getName());
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