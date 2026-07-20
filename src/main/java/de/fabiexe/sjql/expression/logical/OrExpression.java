package de.fabiexe.sjql.expression.logical;

import de.fabiexe.sjql.expression.Expression;
import org.jetbrains.annotations.NotNull;

/**
 * A logical {@code OR} expression.
 *
 * @param a the left-hand side
 * @param b the right-hand side
 */
public record OrExpression(@NotNull Expression a, @NotNull Expression b) implements LogicalExpression {}