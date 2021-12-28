@file:OptIn(KtorExperimentalLocationsAPI::class)

package io.ktor.samples.kweet

import io.ktor.samples.kweet.dao.DAOFacade
import io.ktor.server.application.call
import io.ktor.server.freemarker.FreeMarkerContent
import io.ktor.server.locations.KtorExperimentalLocationsAPI
import io.ktor.server.locations.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import org.komapper.r2dbc.R2dbc
import org.komapper.tx.r2dbc.withTransaction

/**
 * Register the index route of the website.
 */
fun Route.index(db: R2dbc, dao: DAOFacade) {
    // Uses the location feature to register a get route for '/'.
    get<Index> {
        db.withTransaction {
            // Tries to get the user from the session (null if failure)
            val user = call.sessions.get<KweetSession>()?.let { dao.user(it.userId) }

            // Obtains several list of kweets using different sortings and filters.
            val top = dao.top(10).map { dao.getKweet(it) }

            // Generates an ETag unique string for this route that will be used for caching.
            val etagString =
                user?.userId + "," + top.joinToString { it.id.toString() }
            val etag = etagString.hashCode()

            // Uses FreeMarker to render the page.
            call.respond(
                FreeMarkerContent(
                    "index.ftl",
                    mapOf("top" to top, "user" to user),
                    etag.toString()
                )
            )
        }
    }
}
