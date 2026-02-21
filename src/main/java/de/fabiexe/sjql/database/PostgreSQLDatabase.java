package de.fabiexe.sjql.database;

import de.fabiexe.sjql.Table;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgreSQLDatabase extends BasicDatabase {
    public PostgreSQLDatabase(@NotNull DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public boolean tableExists(@NotNull Table<?> table) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                // In PostgreSQL, identifiers are lowercase by default if not quoted.
                // We use the lowercase name for the check.
                statement.setString(1, table.getName().toLowerCase());
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1) > 0;
                    }
                }
            }
        }
        return false;
    }
}
