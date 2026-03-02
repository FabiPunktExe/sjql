package de.fabiexe.sjql

import de.fabiexe.sjql.column.Column
import de.fabiexe.sjql.expression.Expression

infix fun <T> Query<T>.where(condition: Expression): Query<T> = where(condition)
fun <T> Query<T>.orderBy(expression: Expression, ascending: Boolean = true): Query<T> = orderBy(expression, ascending)
infix fun <T> Query<T>.orderBy(expression: Expression): Query<T> = orderBy(expression, true)
fun <T> Query<T>.orderBy(column: Column<*>, ascending: Boolean = true): Query<T> = orderBy(column, ascending)
infix fun <T> Query<T>.orderBy(column: Column<*>): Query<T> = orderBy(column, true)
infix fun <T> Query<T>.limit(limit: Long): Query<T> = limit(limit)