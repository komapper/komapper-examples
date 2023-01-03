package io.ktor.samples.kweet

import io.ktor.samples.kweet.dao.DAOFacade
import io.ktor.server.application.call
import io.ktor.server.freemarker.FreeMarkerContent
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import org.komapper.r2dbc.R2dbcDatabase

/**
 * Registers the [ViewKweet] route. (/kweet/{id})
 */
fun Route.viewKweet(db: R2dbcDatabase, dao: DAOFacade, hashFunction: (String) -> String) {
    /**
     * This page shows the [Kweet] content and its replies.
     * If there is an user logged in, and the kweet is from her/him, it will provide secured links to remove it.
     */
    get<ViewKweet> { resource ->
        db.withTransaction { _ ->
            val user = call.sessions.get<KweetSession>()?.let { dao.user(it.userId) }
            val date = System.currentTimeMillis()
            val code = if (user != null) call.securityCode(date, user, hashFunction) else null
            val kweet = dao.getKweet(resource.id)
            val isOwner = user?.userId == kweet.userId

            call.respond(
                FreeMarkerContent(
                    "view-kweet.ftl",
                    mapOf(
                        "user" to user,
                        "kweet" to kweet,
                        "isOwner" to isOwner,
                        "date" to date,
                        "code" to code,
                    ),
                ),
            )
        }
    }
}
