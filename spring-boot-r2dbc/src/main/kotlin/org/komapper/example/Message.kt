package org.komapper.example

import org.komapper.annotation.KmAutoIncrement
import org.komapper.annotation.KmEntityDef
import org.komapper.annotation.KmId

data class Message(
    val id: Int? = null,
    val text: String
)

@KmEntityDef(Message::class)
data class MessageDef(
    @KmId @KmAutoIncrement
    val id: Nothing,
    val text: Nothing
) {
    companion object
}
