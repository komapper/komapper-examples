@file:OptIn(KtorExperimentalLocationsAPI::class)

package io.ktor.samples.kweet

import io.ktor.http.HttpStatusCode
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
 * Register the [UserPage] route '/user/{user}',
 * with the user profile.
 */
fun Route.userPage(db: R2dbc, dao: DAOFacade) {
    /**
     * A GET request will return a page with the profile of a given user from its [UserPage.user] name.
     * If the user doesn't exists, it will return a 404 page instead.
     */
    get<UserPage> {
        db.withTransaction {
            val user = call.sessions.get<KweetSession>()?.let { dao.user(it.userId) }
            val pageUser = dao.user(it.user)

            if (pageUser == null) {
                call.respond(HttpStatusCode.NotFound.description("User ${it.user} doesn't exist"))
            } else {
                val kweets = dao.userKweets(it.user)
                val etag = (user?.userId ?: "") + "_" + kweets.map { it.text.hashCode() }.hashCode().toString()

                call.respond(
                    FreeMarkerContent(
                        "user.ftl",
                        mapOf("user" to user, "pageUser" to pageUser, "kweets" to kweets),
                        etag
                    )
                )
            }
        }
    }
}