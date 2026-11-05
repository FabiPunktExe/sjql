package de.fabiexe.sjql.column

import de.fabiexe.sjql.expression.Expression

infix fun <T> ColumnRef<T>.eq(other: Column<*>): Expression = eq(other)
infix fun <T> ColumnRef<T>.neq(other: Column<*>): Expression = neq(other)
infix fun <T> ColumnRef<T>.gt(other: Column<*>): Expression = gt(other)
infix fun <T> ColumnRef<T>.gte(other: Column<*>): Expression = gte(other)
infix fun <T> ColumnRef<T>.lt(other: Column<*>): Expression = lt(other)
infix fun <T> ColumnRef<T>.lte(other: Column<*>): Expression = lte(other)