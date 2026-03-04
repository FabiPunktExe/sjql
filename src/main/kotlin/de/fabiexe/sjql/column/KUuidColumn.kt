package de.fabiexe.sjql.column

import de.fabiexe.sjql.Table
import java.util.*
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid
import kotlin.uuid.toKotlinUuid

@OptIn(ExperimentalUuidApi::class)
class KUuidColumn(table: Table<*>, name: String) : ComplexColumn<Uuid?, UUID>(
    Uuid::class.java,
    UUID::class.java,
    table,
    name
) {
    override fun toComplex(value: UUID?): Uuid? = value?.toKotlinUuid()
    override fun toBase(value: Uuid?): UUID? = value?.toJavaUuid()
}
