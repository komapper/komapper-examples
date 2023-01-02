package io.ktor.samples.kweet.dao

import io.ktor.samples.kweet.model.Kweet
import io.ktor.samples.kweet.model.User
import java.io.Closeable
import java.time.LocalDateTime

/**
 * A DAO Facade interface for the Database. This allows to provide several implementations.
 *
 * In this case this is used to provide a Database-based implementation using Komapper.
 */
interface DAOFacade : Closeable {
    /**
     * Initializes all the required data.
     * In this case this should initialize the Users and Kweets tables.
     */
    fun init()

    /**
     * Counts the number of replies of a kweet identified by its [id].
     */
    suspend fun countReplies(id: Int): Int

    /**
     * Creates a Kweet from a specific [user] name, the kweet [text] content,
     * an optional [replyTo] id of the parent kweet, and a [date] that would default to the current time.
     */
    suspend fun createKweet(
        user: String,
        text: String,
        replyTo: Int? = null,
        date: LocalDateTime = LocalDateTime.now(),
    ): Int

    /**
     * Deletes a kweet from its [id].
     */
    suspend fun deleteKweet(id: Int)

    /**
     * Get the DAO object representation of a kweet based from its [id].
     */
    suspend fun getKweet(id: Int): Kweet

    /**
     * Obtains a list of kweet from a specific user identified by its [userId].
     */
    suspend fun userKweets(userId: String): List<Kweet>

    /**
     * Tries to get an user from its [userId] and optionally its password [hash].
     * If the [hash] is specified, the password [hash] must match, or the function will return null.
     * If no [hash] is specified, it will return the [User] if exists, or null otherwise.
     */
    suspend fun user(userId: String, hash: String? = null): User?

    /**
     * Tries to get an user from its [email].
     *
     * Returns null if no user has this [email] associated.
     */
    suspend fun userByEmail(email: String): User?

    /**
     * Creates a new [user] in the database from its object [User] representation.
     */
    suspend fun createUser(user: User)

    /**
     * Returns a list of Kweet ids, with the ones with most replies first.
     */
    suspend fun top(count: Int = 10): List<Int>
}
