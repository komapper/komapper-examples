package io.ktor.samples.kweet.dao

import io.ktor.samples.kweet.model.Kweet
import org.komapper.annotation.KomapperAutoIncrement
import org.komapper.annotation.KomapperEntityDef
import org.komapper.annotation.KomapperId

@KomapperEntityDef(Kweet::class, aliases = ["kweet", "kweet2"])
data class KweetDef(
    @KomapperId
    @KomapperAutoIncrement
    val id: Nothing,
    val userId: Nothing,
    val text: Nothing,
    val date: Nothing,
    val replyTo: Nothing,
)
