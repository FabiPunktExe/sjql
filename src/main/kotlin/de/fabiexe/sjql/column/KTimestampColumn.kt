package de.fabiexe.sjql.column

import de.fabiexe.sjql.Table
import kotlin.time.Instant
import kotlin.time.toJavaInstant
import kotlin.time.toKotlinInstant

class KTimestampColumn(table: Table<*>, name: String) : ComplexColumn<Instant?, java.time.Instant>(
    Instant::class.java,
    java.time.Instant::class.java,
    table,
    name
) {
    override fun toComplex(value: java.time.Instant?): Instant? = value?.toKotlinInstant()
    override fun toBase(value: Instant?): java.time.Instant? = value?.toJavaInstant()
}
