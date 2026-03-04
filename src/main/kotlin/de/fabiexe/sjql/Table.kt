package de.fabiexe.sjql

import de.fabiexe.sjql.column.Column
import de.fabiexe.sjql.column.KTimestampColumn
import de.fabiexe.sjql.column.KUuidColumn
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun Table<*>.kUuidColumn(name: String): Column<Uuid?> {
    return column(KUuidColumn(this, name))
}

fun Table<*>.kTimestampColumn(name: String): Column<Instant?> {
    return column(KTimestampColumn(this, name))
}