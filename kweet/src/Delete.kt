@file:OptIn(KtorExperimentalLocationsAPI::class)

package io.ktor.samples.kweet

import io.ktor.http.Parameters
import io.ktor.samples.kweet.dao.DAOFacade
import io.ktor.server.application.call
import io.ktor.server.locations.KtorExperimentalLocationsAPI
import io.ktor.server.locations.post
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import org.komapper.r2dbc.R2dbcDatabase

/**
 * Registers a route for deleting deleting kweets.
 */
fun Route.delete(db: R2dbcDatabase, dao: DAOFacade, hashFunction: (String) -> String) {
    // Uses the location feature to register a post route for '/kweet/{id}/delete'.
    post<KweetDelete> { location ->
        db.withTransaction { _ ->
            // Tries to get (null on failure) the user associated to the current KweetSession
            val user = call.sessions.get<KweetSession>()?.let { dao.user(it.userId) }

            // Receives the Parameters date and code, if any of those fails to be obtained,
            // it redirects to the tweet page without deleting the kweet.
            val post = call.receive<Parameters>()
            val date = post["date"]?.toLongOrNull() ?: return@withTransaction call.redirect(ViewKweet(location.id))
            val code = post["code"] ?: return@withTransaction call.redirect(ViewKweet(location.id))
            val kweet = dao.getKweet(location.id)

            // Verifies that the kweet user matches the session user and that the code and the date matches, to prevent CSFR.
            if (user == null || kweet.userId != user.userId || !call.verifyCode(date, user, code, hashFunction)) {
                call.redirect(ViewKweet(location.id))
            } else {
                dao.deleteKweet(location.id)
                call.redirect(Index())
            }
        }
    }
}
