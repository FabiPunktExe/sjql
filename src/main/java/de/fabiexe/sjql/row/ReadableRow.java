package de.fabiexe.sjql.row;

import de.fabiexe.sjql.column.Column;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ReadableRow<T> {
    <U> @Nullable U get(@NotNull Column<U> column);
    boolean contains(@NotNull Column<?> column);
}
