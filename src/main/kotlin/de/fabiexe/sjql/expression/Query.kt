package de.fabiexe.sjql.expression

import de.fabiexe.sjql.Query
import de.fabiexe.sjql.column.Column

infix fun <T> Query<T>.where(condition: Expression): Query<T> = where(condition)
fun <T> Query<T>.orderBy(column: Column<*>, ascending: Boolean = true): Query<T> = orderBy(column, ascending)
infix fun <T> Query<T>.orderBy(column: Column<*>): Query<T> = orderBy(column, true)
infix fun <T> Query<T>.limit(limit: Long): Query<T> = limit(limit)