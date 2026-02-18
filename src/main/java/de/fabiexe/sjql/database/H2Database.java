package de.fabiexe.sjql.database;

import de.fabiexe.sjql.Table;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class H2Database extends BasicDatabase {
    public H2Database(@NotNull DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public boolean tableExists(@NotNull Table<?> table) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String tableName = table.name().toUpperCase();
            String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '" + tableName + "'";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1) > 0;
                    } else {
                        return false;
                    }
                }
            }
        }
    }
}
