package de.fabiexe.sjql.expression.dynamic;

import de.fabiexe.sjql.column.Column;
import org.jetbrains.annotations.NotNull;

public record ColumnExpression<T>(@NotNull Column<T> column) implements DynamicExpression {}
