package de.fabiexe.sjql.database;

import org.junit.jupiter.api.io.TempDir;
import org.sqlite.SQLiteDataSource;

import java.nio.file.Path;

public class SQLiteTest extends AbstractDatabaseTest {
    @TempDir
    static Path tempDir;

    @Override
    protected SQLiteDataSource createDataSource() {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:" + tempDir.resolve("test.db"));
        return dataSource;
    }
}
