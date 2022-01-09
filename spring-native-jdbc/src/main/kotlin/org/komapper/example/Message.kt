package org.komapper.example

import kotlinx.serialization.Serializable
import org.komapper.annotation.KomapperAutoIncrement
import org.komapper.annotation.KomapperEntityDef
import org.komapper.annotation.KomapperId

@Serializable
data class Message(
    val id: Int? = null,
    val text: String
)

@KomapperEntityDef(Message::class)
data class MessageDef(
    @KomapperId @KomapperAutoIncrement
    val id: Nothing,
)
