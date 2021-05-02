package org.komapper.example

import org.komapper.annotation.KmAutoIncrement
import org.komapper.annotation.KmColumn
import org.komapper.annotation.KmCreatedAt
import org.komapper.annotation.KmEntity
import org.komapper.annotation.KmId
import org.komapper.annotation.KmUpdatedAt
import org.komapper.annotation.KmVersion
import java.time.LocalDateTime

@KmEntity
data class Address(
    @KmId @KmAutoIncrement @KmColumn(name = "ADDRESS_ID")
    val id: Int = 0,
    val street: String,
    @KmVersion val version: Int = 0,
    @KmCreatedAt val createdAt: LocalDateTime? = null,
    @KmUpdatedAt val updatedAt: LocalDateTime? = null,
) {
    companion object
}
