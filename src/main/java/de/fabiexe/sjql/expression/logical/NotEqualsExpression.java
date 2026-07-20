package de.fabiexe.sjql.expression.logical;

import de.fabiexe.sjql.expression.Expression;
import org.jetbrains.annotations.NotNull;

/**
 * An inequality ({@code !=}) expression.
 *
 * @param a the left-hand side
 * @param b the right-hand side
 */
public record NotEqualsExpression(@NotNull Expression a, @NotNull Expression b) implements LogicalExpression {}