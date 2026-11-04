package de.fabiexe.sjql

import de.fabiexe.sjql.column.Column
import de.fabiexe.sjql.expression.Expression

infix fun <T : Any> Query<T>.where(condition: Expression): Query<T> = where(condition)
fun <T : Any> Query<T>.orderBy(expression: Expression, ascending: Boolean = true): Query<T> = orderBy(expression, ascending)
infix fun <T : Any> Query<T>.orderBy(expression: Expression): Query<T> = orderBy(expression, true)
fun <T : Any> Query<T>.orderBy(column: Column<*>, ascending: Boolean = true): Query<T> = orderBy(column, ascending)
infix fun <T : Any> Query<T>.orderBy(column: Column<*>): Query<T> = orderBy(column, true)
infix fun <T : Any> Query<T>.limit(limit: Long): Query<T> = limit(limit)