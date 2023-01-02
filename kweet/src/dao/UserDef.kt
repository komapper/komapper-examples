package io.ktor.samples.kweet.dao

import io.ktor.samples.kweet.model.User
import org.komapper.annotation.KomapperColumn
import org.komapper.annotation.KomapperEntityDef
import org.komapper.annotation.KomapperId
import org.komapper.annotation.KomapperTable

@KomapperEntityDef(User::class)
@KomapperTable(alwaysQuote = true)
data class UserDef(
    @KomapperId
    val userId: Nothing,
    val email: Nothing,
    val displayName: Nothing,
    @KomapperColumn(masking = true)
    val passwordHash: Nothing,
)
