package de.fabiexe.sjql.database;

import de.fabiexe.sjql.Database;
import de.fabiexe.sjql.Query;
import de.fabiexe.sjql.Statement;
import de.fabiexe.sjql.Table;
import de.fabiexe.sjql.column.Column;
import de.fabiexe.sjql.column.DoubleColumn;
import de.fabiexe.sjql.column.IntColumn;
import de.fabiexe.sjql.column.StringColumn;
import de.fabiexe.sjql.expression.constant.ConstantExpression;
import de.fabiexe.sjql.query.BasicDeleteStatement;
import de.fabiexe.sjql.query.BasicRowQuery;
import de.fabiexe.sjql.query.BasicValueQuery;
import de.fabiexe.sjql.row.ReadableRow;
import de.fabiexe.sjql.row.WritableRow;
import de.fabiexe.sjql.util.SQLUtil;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public abstract class BasicDatabase implements Database {
    protected final DataSource dataSource;

    public BasicDatabase(@NotNull DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public @NotNull DataSource getDataSource() {
        return dataSource;
    }

    protected @NotNull String getColumnType(Column<?> column) {
        return switch (column) {
            case IntColumn _ -> "INT";
            case DoubleColumn _ -> "DOUBLE";
            case StringColumn stringColumn -> "VARCHAR(" + stringColumn.getMaxLength() + ")";
        };
    }

    @Override
    public void createTable(@NotNull Table<?> table) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                    .append(table.name())
                    .append(" (");

            List<Column<?>> columns = table.getColumns();
            for (int i = 0; i < columns.size(); i++) {
                Column<?> column = columns.get(i);
                sql.append(column.name()).append(' ').append(getColumnType(column));
                if (i < columns.size() - 1) {
                    sql.append(", ");
                }
            }
            sql.append(");");

            try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
                statement.executeUpdate();
            }
        }
    }

    @Override
    public void deleteTable(@NotNull Table<?> table) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "DROP TABLE IF EXISTS " + table.name();
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.execute();
            }
        }
    }

    @Override
    public <T> @NotNull Statement delete(@NotNull Table<T> table) {
        return new BasicDeleteStatement<>(table, dataSource::getConnection);
    }

    @Override
    public <T> void insert(@NotNull Table<T> table, @NotNull WritableRow row) throws SQLException {
        for (Column<?> column : table.getColumns()) {
            if (column.defaultValue() == null && !row.contains(column)) {
                throw new IllegalArgumentException("Column " + column.name() + " is required but not set in the row");
            }
        }

        try (Connection connection = dataSource.getConnection()) {
            StringBuilder sql = new StringBuilder("INSERT INTO ")
                    .append(table.name())
                    .append(" (");

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
                sql.append(column.name());
            }

            sql.append(") VALUES (");

            first = true;
            for (Column<?> column : table.getColumns()) {
                if (!row.contains(column)) {
                    continue;
                }
                if (first) {
                    first = false;
                } else {
                    sql.append(", ");
                }
                sql.append("?");
            }

            sql.append(");");

            try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
                int index = 1;
                for (Column<?> column : table.getColumns()) {
                    if (!row.contains(column)) {
                        continue;
                    }
                    if (row.get(column) instanceof ConstantExpression<?> expression) {
                        SQLUtil.setObject(statement, index++, expression);
                    }
                }
                statement.executeUpdate();
            }
        }
    }

    @Override
    public @NotNull <T> Query<List<ReadableRow<T>>> selectRows(@NotNull Table<T> table) {
        return new BasicRowQuery<>(table, dataSource::getConnection);
    }

    @Override
    public @NotNull <T> Query<List<T>> select(@NotNull Table<T> table) {
        return new BasicValueQuery<>(table, dataSource::getConnection);
    }
}
