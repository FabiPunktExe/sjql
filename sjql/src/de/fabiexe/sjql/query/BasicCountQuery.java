package de.fabiexe.sjql.query;

import de.fabiexe.sjql.Query;
import de.fabiexe.sjql.database.BasicDatabase;
import de.fabiexe.sjql.expression.constant.ConstantExpression;
import de.fabiexe.sjql.util.SQLUtil;
import org.jspecify.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A query that counts the number of matching rows.
 *
 * @param <T> the type of the objects that represent rows in the queried table
 */
public class BasicCountQuery<T extends @Nullable Object> extends BasicQuery<T, Long> {
    /**
     * Creates a count query that copies the modifiers from another query.
     *
     * @param database the database to execute the query against
     * @param query the query whose modifiers should be copied
     */
    public BasicCountQuery(BasicDatabase database, BasicQuery<T, ?> query) {
        super(database, query.table, query.connectionSupplier);
        this.condition = query.condition;
        this.ordering = query.ordering;
        this.limit = query.limit;
    }

    @Override
    public Query<Long> count() {
        return new ConstantQuery<>(1L);
    }

    @Override
    public Long execute() throws SQLException {
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

    private Map.Entry<String, List<ConstantExpression<?>>> buildSql() {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM ").append(database.escapeTableName(table.getName()));
        List<ConstantExpression<?>> parameters = new ArrayList<>();
        if (condition != null) {
            sql.append(" WHERE ");
            Map.Entry<String, List<ConstantExpression<?>>> conditionSql = SQLUtil.buildSql(database, condition);
            sql.append(conditionSql.getKey());
            parameters.addAll(conditionSql.getValue());
        }
        if (limit != null) {
            sql.append(" LIMIT ").append(limit);
        }
        return Map.entry(sql.toString(), parameters);
    }
}