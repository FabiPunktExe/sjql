package de.fabiexe.sjql;

import java.sql.SQLException;

/** Represents a generic SQL statement. */
public interface Statement {
    /**
     * Executes the SQL statement.
     *
     * @throws SQLException If a database error occurs during execution
     */
    void execute() throws SQLException;
}
