package de.fabiexe.sjql.database;

import de.fabiexe.sjql.Table;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLiteDatabase extends BasicDatabase {
    public SQLiteDatabase(@NotNull DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public boolean tableExists(@NotNull Table<?> table) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='" + table.getName() + "'";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();
                return resultSet.next() && resultSet.getInt(1) > 0;
            }
        }
    }
}
