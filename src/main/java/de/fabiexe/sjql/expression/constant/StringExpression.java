package de.fabiexe.sjql.expression.constant;

/**
 * A constant string expression.
 *
 * @param value the string value
 */
public record StringExpression(String value) implements ConstantExpression<String> {}