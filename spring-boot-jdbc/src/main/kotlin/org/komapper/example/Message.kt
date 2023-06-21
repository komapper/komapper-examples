package org.komapper.example

import org.komapper.annotation.EnumType
import org.komapper.annotation.KomapperAutoIncrement
import org.komapper.annotation.KomapperEntity
import org.komapper.annotation.KomapperEnum
import org.komapper.annotation.KomapperId

@KomapperEntity
data class Message(
    @KomapperId
    @KomapperAutoIncrement
    val id: Int? = null,
    val text: String,
    @KomapperEnum(EnumType.PROPERTY, hint = "code")
    val priority: Priority,
)

enum class Priority(val code: String) {
    HIGH("H"), LOW("L")
}
