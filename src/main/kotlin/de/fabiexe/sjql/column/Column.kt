package de.fabiexe.sjql.column

import de.fabiexe.sjql.expression.Expression

infix fun <T> Column<T>.eq(expression: Expression) = eq(expression)
infix fun <T> Column<T>.eq(other: Column<T>) = eq(other)
infix fun <T> Column<T>.neq(expression: Expression) = neq(expression)
infix fun <T> Column<T>.neq(other: Column<T>) = neq(other)
infix fun <T> Column<T>.gt(expression: Expression) = gt(expression)
infix fun <T> Column<T>.gt(other: Column<T>) = gt(other)
infix fun <T> Column<T>.gte(expression: Expression) = gte(expression)
infix fun <T> Column<T>.gte(other: Column<T>) = gte(other)
infix fun <T> Column<T>.lt(expression: Expression) = lt(expression)
infix fun <T> Column<T>.lt(other: Column<T>) = lt(other)
infix fun <T> Column<T>.lte(expression: Expression) = lte(expression)
infix fun <T> Column<T>.lte(other: Column<T>) = lte(other)