package de.fabiexe.sjql;

import de.fabiexe.sjql.column.Column;
import de.fabiexe.sjql.expression.Expression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface Query<T> {
    @NotNull Query<T> where(@NotNull Expression condition);
    @NotNull Query<T> orderBy(@NotNull Column<?> column, boolean ascending);
    @NotNull Query<T> orderBy(@NotNull List<Map.Entry<Column<?>, Boolean>> ordering);
    @NotNull Query<T> limit(@Nullable Long limit);
    @NotNull Query<Long> count();
    @Nullable T execute() throws SQLException;
}
