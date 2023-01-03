package io.ktor.samples.kweet

import io.ktor.http.Parameters
import io.ktor.samples.kweet.dao.DAOFacade
import io.ktor.server.application.call
import io.ktor.server.freemarker.FreeMarkerContent
import io.ktor.server.request.receive
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import org.komapper.r2dbc.R2dbcDatabase

/**
 * Register routes for the [PostNew] route '/post-new'
 */
fun Route.postNew(db: R2dbcDatabase, dao: DAOFacade, hashFunction: (String) -> String) {
    /**
     * A GET request returns a page with a form to post a new Kweet in the case the user
     * is logged also generating a [code] token to prevent.
     *
     * If the user is not logged it redirects to the [Login] page.
     */
    get<PostNew> { _ ->
        db.withTransaction { _ ->
            val user = call.sessions.get<KweetSession>()?.let { dao.user(it.userId) }

            if (user == null) {
                call.redirect(Login())
            } else {
                val date = System.currentTimeMillis()
                val code = call.securityCode(date, user, hashFunction)

                call.respond(
                    FreeMarkerContent(
                        "new-kweet.ftl",
                        mapOf("user" to user, "date" to date, "code" to code),
                        user.userId,
                    ),
                )
            }
        }
    }
    /**
     * A POST request actually tries to create a new [Kweet].
     * It validates the `date`, `code` and `text` parameters and redirects to the login page on failure.
     * On success it creates the new [Kweet] and redirect to the [ViewKweet] page to view that specific Kweet.
     */
    post<PostNew> {
        db.withTransaction {
            val user = call.sessions.get<KweetSession>()?.let { dao.user(it.userId) }

            val post = call.receive<Parameters>()
            val date = post["date"]?.toLongOrNull() ?: return@withTransaction call.redirect(it)
            val code = post["code"] ?: return@withTransaction call.redirect(it)
            val text = post["text"] ?: return@withTransaction call.redirect(it)

            if (user == null || !call.verifyCode(date, user, code, hashFunction)) {
                call.redirect(Login())
            } else {
                val id = dao.createKweet(user.userId, text, null)
                call.redirect(ViewKweet(id))
            }
        }
    }
}
