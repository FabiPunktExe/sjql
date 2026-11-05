package de.fabiexe.sjql.query;

import de.fabiexe.sjql.Table;
import de.fabiexe.sjql.TableRef;
import de.fabiexe.sjql.column.Column;
import de.fabiexe.sjql.column.ColumnRef;
import de.fabiexe.sjql.database.BasicDatabase;
import de.fabiexe.sjql.expression.Expression;
import de.fabiexe.sjql.expression.constant.ConstantExpression;
import de.fabiexe.sjql.row.BasicReadableRow;
import de.fabiexe.sjql.row.ReadableRow;
import de.fabiexe.sjql.util.SQLUtil;
import de.fabiexe.sjql.util.ThrowingSupplier;
import org.jspecify.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** A query that returns raw {@link ReadableRow} instances. */
public class BasicRowQuery extends BasicQuery<List<ReadableRow>> {
    /**
     * Creates a new row query for the given tables.
     *
     * @param database The database to query
     * @param tables The tables to query
     * @param connectionSupplier A supplier for the database connection
     */
    public BasicRowQuery(
            BasicDatabase database,
            List<TableRef<?>> tables,
            ThrowingSupplier<Connection, SQLException> connectionSupplier
    ) {
        super(database, tables, connectionSupplier);
    }

    /**
     * Creates a new row query for the given table.
     *
     * @param database The database to query
     * @param table The table to query
     * @param connectionSupplier A supplier for the database connection
     */
    public BasicRowQuery(
            BasicDatabase database,
            Table<?> table,
            ThrowingSupplier<Connection, SQLException> connectionSupplier
    ) {
        this(database, List.of(table.ref()), connectionSupplier);
    }

    @Override
    public List<ReadableRow> execute() throws SQLException {
        try (Connection connection = connectionSupplier.get()) {
            Map.Entry<String, List<ConstantExpression<?>>> sql = buildSql();
            List<ConstantExpression<?>> parameters = sql.getValue();
            try (PreparedStatement statement = connection.prepareStatement(sql.getKey())) {
                for (int i = 0; i < parameters.size(); i++) {
                    SQLUtil.setObject(statement, i + 1, parameters.get(i));
                }
                ResultSet resultSet = statement.executeQuery();
                List<ReadableRow> result = new ArrayList<>();
                while (resultSet.next()) {
                    Map<ColumnRef<?>, @Nullable Object> rowContent = new HashMap<>();
                    int i = 0;
                    for (TableRef<?> tableRef : tables) {
                        List<Column<?>> columns = tableRef.getTable().getColumns();
                        for (int j = 0; j < columns.size(); i++, j++) {
                            Column<?> column = columns.get(j);
                            rowContent.put(tableRef.column(column), SQLUtil.getObject(resultSet, i + 1, column));
                        }
                    }
                    result.add(new BasicReadableRow(tables, rowContent));
                }
                return result;
            }
        }
    }

    private Map.Entry<String, List<ConstantExpression<?>>> buildSql() {
        StringBuilder sql = new StringBuilder("SELECT * FROM ")
                .append(SQLUtil.buildFromClause(database, tables));
        List<ConstantExpression<?>> parameters = new ArrayList<>();
        if (condition != null) {
            sql.append(" WHERE ");
            Map.Entry<String, List<ConstantExpression<?>>> conditionSql = SQLUtil.buildSql(database, condition);
            sql.append(conditionSql.getKey());
            parameters.addAll(conditionSql.getValue());
        }
        if (!ordering.isEmpty()) {
            sql.append(" ORDER BY ");
            for (int i = 0; i < ordering.size(); i++) {
                Map.Entry<Expression, Boolean> entry = ordering.get(i);
                Map.Entry<String, List<ConstantExpression<?>>> orderSql = SQLUtil.buildSql(database, entry.getKey());
                sql.append(orderSql.getKey()).append(entry.getValue() ? " ASC" : " DESC");
                parameters.addAll(orderSql.getValue());
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