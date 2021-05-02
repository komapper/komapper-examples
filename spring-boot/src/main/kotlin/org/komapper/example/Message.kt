package org.komapper.example

import org.komapper.annotation.KmAutoIncrement
import org.komapper.annotation.KmEntity
import org.komapper.annotation.KmId

@KmEntity
data class Message(
    @KmId @KmAutoIncrement
    val id: Int? = null,
    val text: String
) {
    companion object
}
