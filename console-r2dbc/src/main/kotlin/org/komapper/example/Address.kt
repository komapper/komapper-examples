package org.komapper.example

import org.komapper.annotation.KomapperAutoIncrement
import org.komapper.annotation.KomapperColumn
import org.komapper.annotation.KomapperCreatedAt
import org.komapper.annotation.KomapperEntity
import org.komapper.annotation.KomapperId
import org.komapper.annotation.KomapperUpdatedAt
import org.komapper.annotation.KomapperVersion
import java.time.LocalDateTime

@KomapperEntity
data class Address(
    @KomapperId
    @KomapperAutoIncrement
    @KomapperColumn(name = "ADDRESS_ID")
    val id: Int = 0,
    val street: String,
    @KomapperVersion val version: Int = 0,
    @KomapperCreatedAt val createdAt: LocalDateTime? = null,
    @KomapperUpdatedAt val updatedAt: LocalDateTime? = null,
)
