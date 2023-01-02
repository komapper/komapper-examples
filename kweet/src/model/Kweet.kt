package io.ktor.samples.kweet.model

import java.io.Serializable
import java.time.LocalDateTime

data class Kweet(
    val id: Int,
    val userId: String,
    val text: String,
    val date: LocalDateTime,
    val replyTo: Int?,
) : Serializable
