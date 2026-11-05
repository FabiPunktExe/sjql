package de.fabiexe.sjql

import de.fabiexe.sjql.column.Column
import de.fabiexe.sjql.column.ColumnRef

operator fun <T> TableRef<*>.get(column: Column<T>): ColumnRef<T> = column(column)