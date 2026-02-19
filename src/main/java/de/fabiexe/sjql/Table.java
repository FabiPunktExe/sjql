package de.fabiexe.sjql;

import de.fabiexe.sjql.column.*;
import de.fabiexe.sjql.row.BasicWritableRow;
import de.fabiexe.sjql.row.ConstructorRowMapper;
import de.fabiexe.sjql.row.ReadableRow;
import de.fabiexe.sjql.row.WritableRow;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class Table<T> {
    private final String name;
    private final Class<T> type;
    private final List<Column<?>> columns = new ArrayList<>();
    private Function<ReadableRow<T>, T> rowMapper;

    public Table(
            @NotNull String name,
            @NotNull Class<T> type,
            @NotNull Function<ReadableRow<T>, T> rowMapper
    ) {
        this.name = name;
        this.type = type;
        this.rowMapper = rowMapper;
    }

    public Table(
            @NotNull String name,
            @NotNull Class<T> type
    ) {
        this.name = name;
        this.type = type;
        this.rowMapper = null;
    }

    public <U> @NotNull Column<U> column(@NotNull Column<U> column) {
        columns.add(column);
        rowMapper = null;
        return column;
    }

    public @NotNull Column<Integer> intColumn(@NotNull String name) {
        return column(new IntColumn(this, name));
    }

    public @NotNull Column<Double> doubleColumn(@NotNull String name) {
        return column(new DoubleColumn(this, name));
    }

    public @NotNull Column<String> stringColumn(@NotNull String name, int length) {
        return column(new StringColumn(this, name, length));
    }

    public @NotNull Column<Long> longColumn(@NotNull String name) {
        return column(new LongColumn(this, name));
    }

    public @NotNull Column<Float> floatColumn(@NotNull String name) {
        return column(new FloatColumn(this, name));
    }

    public @NotNull Column<Boolean> booleanColumn(@NotNull String name) {
        return column(new BooleanColumn(this, name));
    }

    public @NotNull Column<UUID> uuidColumn(@NotNull String name) {
        return column(new UUIDColumn(this, name));
    }

    public void insert(@NotNull Consumer<WritableRow> builder) throws SQLException {
        WritableRow row = new BasicWritableRow<>(this);
        builder.accept(row);
        Database.CURRENT_DATABASE.get().insert(this, row);
    }

    public @NotNull DeleteStatement delete() {
        return (DeleteStatement) Database.CURRENT_DATABASE.get().delete(this);
    }

    public @NotNull Query<List<T>> select() {
        return Database.CURRENT_DATABASE.get().select(this);
    }

    public long count() throws SQLException {
        return select().count().executeNotNull();
    }

    public @NotNull String name() {
        return name;
    }

    public @NotNull Class<T> getType() {
        return type;
    }

    public @NotNull Function<ReadableRow<T>, T> getRowMapper() {
        if (rowMapper == null) {
            rowMapper = new ConstructorRowMapper<>(type, columns.toArray(Column<?>[]::new));
        }
        return rowMapper;
    }

    public @NotNull List<Column<?>> getColumns() {
        return columns;
    }

    public boolean hasColumn(@NotNull Column<?> column) {
        return columns.contains(column);
    }
}
