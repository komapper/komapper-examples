@file:OptIn(KtorExperimentalLocationsAPI::class)

package io.ktor.samples.kweet

import io.ktor.http.Parameters
import io.ktor.samples.kweet.dao.DAOFacade
import io.ktor.samples.kweet.model.User
import io.ktor.server.application.application
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.freemarker.FreeMarkerContent
import io.ktor.server.locations.KtorExperimentalLocationsAPI
import io.ktor.server.locations.get
import io.ktor.server.locations.post
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set
import org.komapper.r2dbc.R2dbc
import org.komapper.tx.r2dbc.withTransaction

/**
 * Register routes for user registration in the [Register] route (/register)
 */
fun Route.register(db: R2dbc, dao: DAOFacade, hashFunction: (String) -> String) {
    /**
     * A POST request to the [Register] route, will try to create a new user.
     *
     * - If the user is already logged, it redirects to the [UserPage] page.
     * - If not specified the userId, password, displayName or email, it will redirect to the [Register] form page.
     * - Then it will validate the specified parameters, redirecting displaying an error to the [Register] page.
     * - On success, it generates a new [User]. But instead of storing the password plain text,
     *   it stores a hash of the password.
     */
    post<Register> {
        db.withTransaction {
            // get current from session data if any
            val user = call.sessions.get<KweetSession>()?.let { dao.user(it.userId) }
            // user already logged in? redirect to user page.
            if (user != null) return@withTransaction call.redirect(UserPage(user.userId))

            // receive post data
            // TODO: use conneg when it's ready and `call.receive<Register>()`
            val registration = call.receive<Parameters>()
            val userId = registration["userId"] ?: return@withTransaction call.redirect(it)
            val password = registration["password"] ?: return@withTransaction call.redirect(it)
            val displayName = registration["displayName"] ?: return@withTransaction call.redirect(it)
            val email = registration["email"] ?: return@withTransaction call.redirect(it)

            // prepare location class for error if any
            val error = Register(userId, displayName, email)

            when {
                password.length < 6 -> call.redirect(error.copy(error = "Password should be at least 6 characters long"))
                userId.length < 4 -> call.redirect(error.copy(error = "Login should be at least 4 characters long"))
                !userNameValid(userId) -> call.redirect(error.copy(error = "Login should be consists of digits, letters, dots or underscores"))
                dao.user(userId) != null -> call.redirect(error.copy(error = "User with the following login is already registered"))
                else -> {
                    val hash = hashFunction(password)
                    val newUser = User(userId, email, displayName, hash)

                    try {
                        dao.createUser(newUser)
                    } catch (e: Throwable) {
                        when {
                            // NOTE: This is security issue that allows to enumerate/verify registered users. Do not do this in real app :)
                            dao.user(userId) != null -> call.redirect(error.copy(error = "User with the following login is already registered"))
                            dao.userByEmail(email) != null -> call.redirect(error.copy(error = "User with the following email $email is already registered"))
                            else -> {
                                application.log.error("Failed to register user", e)
                                call.redirect(error.copy(error = "Failed to register"))
                            }
                        }
                    }

                    call.sessions.set(KweetSession(newUser.userId))
                    call.redirect(UserPage(newUser.userId))
                }
            }
        }
    }

    /**
     * A GET request would show the registration form (with an error if specified by the URL in the case there was an error in the form processing)
     * If the user is already logged, it redirects the client to the [UserPage] instead.
     */
    get<Register> {
        db.withTransaction {
            val user = call.sessions.get<KweetSession>()?.let { dao.user(it.userId) }
            if (user != null) {
                call.redirect(UserPage(user.userId))
            } else {
                call.respond(
                    FreeMarkerContent(
                        "register.ftl",
                        mapOf("pageUser" to User(it.userId, it.email, it.displayName, ""), "error" to it.error),
                        ""
                    )
                )
            }
        }
    }
}