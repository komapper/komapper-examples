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
import io.ktor.server.sessions.clear
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set
import org.komapper.r2dbc.R2dbcDatabase

/**
 * Registers the [Login] and [Logout] routes '/login' and '/logout'.
 */
fun Route.login(db: R2dbcDatabase, dao: DAOFacade, hash: (String) -> String) {
    /**
     * A GET request to the [Login], would respond with the login page
     * (unless the user is already logged in, in which case it would redirect to the user's page)
     */
    get<Login> { resource ->
        db.withTransaction {
            val user = call.sessions.get<KweetSession>()?.let { dao.user(it.userId) }

            if (user != null) {
                call.redirect(UserPage(user.userId))
            } else {
                call.respond(
                    FreeMarkerContent(
                        "login.ftl",
                        mapOf("userId" to resource.userId, "error" to resource.error),
                        "",
                    ),
                )
            }
        }
    }

    /**
     * A POST request to the [Login] actually processes the [Parameters] to validate them, if valid it sets the session.
     * It will redirect either to the [Login] page with an error in the case of error,
     * or to the [UserPage] if the login was successful.
     */
    post<Login> {
        db.withTransaction {
            val post = call.receive<Parameters>()
            val userId = post["userId"] ?: return@withTransaction call.redirect(it)
            val password = post["password"] ?: return@withTransaction call.redirect(it)

            val error = Login(userId)

            val login = when {
                userId.length < 4 -> null
                password.length < 6 -> null
                !userNameValid(userId) -> null
                else -> dao.user(userId, hash(password))
            }

            if (login == null) {
                call.redirect(error.copy(error = "Invalid username or password"))
            } else {
                call.sessions.set(KweetSession(login.userId))
                call.redirect(UserPage(login.userId))
            }
        }
    }

    /**
     * A GET request to the [Logout] page, removes the session and redirects to the [Index] page.
     */
    get<Logout> {
        call.sessions.clear<KweetSession>()
        call.redirect(Index())
    }
}
