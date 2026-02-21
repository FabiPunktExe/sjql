package de.fabiexe.sjql.query;

import de.fabiexe.sjql.Query;
import de.fabiexe.sjql.expression.constant.ConstantExpression;
import de.fabiexe.sjql.util.SQLUtil;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BasicCountQuery<T> extends BasicQuery<T, Long> {
    public BasicCountQuery(@NotNull BasicQuery<T, ?> query) {
        super(query.table, query.connectionSupplier);
        condition = query.condition;
        ordering = query.ordering;
        limit = query.limit;
    }

    @Override
    public @NotNull Query<Long> count() {
        return new ConstantQuery<>(1L);
    }

    @Override
    public @NotNull Long execute() throws SQLException {
        try (Connection connection = connectionSupplier.get()) {
            Map.Entry<String, List<ConstantExpression<?>>> sql = buildSql();
            List<ConstantExpression<?>> parameters = sql.getValue();
            try (PreparedStatement statement = connection.prepareStatement(sql.getKey())) {
                for (int i = 0; i < parameters.size(); i++) {
                    SQLUtil.setObject(statement, i + 1, parameters.get(i));
                }
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getLong(1);
                } else {
                    return 0L;
                }
            }
        }
    }

    private @NotNull Map.Entry<String, List<ConstantExpression<?>>> buildSql() {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM ").append(table.getName());
        List<ConstantExpression<?>> parameters = new ArrayList<>();
        if (condition != null) {
            sql.append(" WHERE ");
            Map.Entry<String, List<ConstantExpression<?>>> conditionSql = SQLUtil.buildSql(condition);
            sql.append(conditionSql.getKey());
            parameters.addAll(conditionSql.getValue());
        }
        if (limit != null) {
            sql.append(" LIMIT ").append(limit);
        }
        return Map.entry(sql.toString(), parameters);
    }
}
