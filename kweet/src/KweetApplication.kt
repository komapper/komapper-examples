package io.ktor.samples.kweet

import freemarker.cache.ClassTemplateLoader
import freemarker.template.Configuration.VERSION_2_3_31
import io.ktor.http.HttpHeaders
import io.ktor.resources.Resource
import io.ktor.samples.kweet.dao.DAOFacade
import io.ktor.samples.kweet.dao.DAOFacadeKomapper
import io.ktor.samples.kweet.model.User
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.install
import io.ktor.server.freemarker.FreeMarker
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.conditionalheaders.ConditionalHeaders
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.plugins.partialcontent.PartialContent
import io.ktor.server.request.header
import io.ktor.server.request.host
import io.ktor.server.resources.Resources
import io.ktor.server.resources.href
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.routing
import io.ktor.server.sessions.SessionTransportTransformerMessageAuthentication
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
import io.ktor.util.hex
import kotlinx.serialization.Serializable
import no.api.freemarker.java8.Java8ObjectWrapper
import org.komapper.r2dbc.R2dbcDatabase
import java.io.File
import java.net.URI
import java.util.concurrent.TimeUnit
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/*
 * Classes are used by the Resources plugin to build URLs and register routes.
 */

@Serializable
@Resource("/")
class Index()

@Serializable
@Resource("/post-new")
class PostNew()

@Serializable
@Resource("/kweet/{id}/delete")
class KweetDelete(val id: Int)

@Serializable
@Resource("/kweet/{id}")
data class ViewKweet(val id: Int)

@Serializable
@Resource("/user/{user}")
data class UserPage(val user: String)

@Serializable
@Resource("/register")
data class Register(
    val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val error: String = "",
)

@Serializable
@Resource("/login")
data class Login(val userId: String = "", val error: String = "")

@Serializable
@Resource("/logout")
class Logout()

/**
 * Represents a session in this site containing the userId.
 */
@Serializable
data class KweetSession(val userId: String)

/**
 * Hardcoded secret hash key used to hash the passwords, and to authenticate the sessions.
 */
private val hashKey = hex("6819b57a326945c1968f45236589")

/**
 * File where the database is going to be stored.
 */
private val dir = File("build/db")

/**
 * HMac SHA1 key spec for the password hashing.
 */
private val hmacKey = SecretKeySpec(hashKey, "HmacSHA1")

/**
 * The R2DBC database.
 */
private val db = R2dbcDatabase("r2dbc:h2:file:///${dir.canonicalFile.absolutePath}")

/**
 * Constructs a facade with the database.
 */
private val dao: DAOFacade = DAOFacadeKomapper(db)

/**
 * Entry Point of the application. This function is referenced in the
 * resources/application.conf file inside the ktor.application.modules.
 *
 * For more information about this file: https://ktor.io/servers/configuration.html#hocon-file
 */
fun Application.main() {
    // First we initialize the database.
    dao.init()
    // Now we call to a main with the dependencies as arguments.
    // Separating this function with its dependencies allows us to provide several modules with
    // the same code and different datasources living in the same application,
    // and to provide mocked instances for doing integration tests.
    mainWithDependencies(db, dao)
}

/**
 * This function is called from the entry point and tests to configure an application
 * using the specified [dao] [DAOFacade].
 */
fun Application.mainWithDependencies(db: R2dbcDatabase, dao: DAOFacade) {
    // This adds automatically Date and Server headers to each response, and would allow you to configure
    // additional headers served to each response.
    install(DefaultHeaders)
    // This uses use the logger to log every call (request/response)
    install(CallLogging)
    // Automatic '304 Not Modified' Responses
    install(ConditionalHeaders)
    // Supports for Range, Accept-Range and Content-Range headers
    install(PartialContent)
    // Allows using classes annotated with @Resource to represent URLs.
    // They are typed, can be constructed to generate URLs, and can be used to register routes.
    install(Resources)
    // Adds support to generate templated responses using FreeMarker.
    // We configure it specifying the path inside the resources to use to get the template files.
    // You can use <!-- @ftlvariable --> to annotate types inside the templates
    // in a way that works with IntelliJ IDEA Ultimate.
    // You can check the `resources/templates/*.ftl` files for reference.
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
        objectWrapper = Java8ObjectWrapper(VERSION_2_3_31)
    }
    // Configure the session to be represented by a [KweetSession],
    // using the SESSION cookie to store it, and transforming it to be authenticated with the [hashKey].
    // it is sent in plain text, but since it is authenticated can't be modified without knowing the secret [hashKey].
    install(Sessions) {
        cookie<KweetSession>("SESSION") {
            transform(SessionTransportTransformerMessageAuthentication(hashKey))
        }
    }

    // Provides a hash function to be used when registering the resources.
    val hashFunction = { s: String -> hash(s) }

    // Register all the routes available to the application.
    // They are split in several methods and files, so it can scale for larger
    // applications keeping a reasonable amount of lines per file.
    routing {
        styles()
        index(db, dao)
        postNew(db, dao, hashFunction)
        delete(db, dao, hashFunction)
        userPage(db, dao)
        viewKweet(db, dao, hashFunction)

        login(db, dao, hashFunction)
        register(db, dao, hashFunction)
    }
}

/**
 * Method that hashes a [password] by using the globally defined secret key [hmacKey].
 */
fun hash(password: String): String {
    val hmac = Mac.getInstance("HmacSHA1")
    hmac.init(hmacKey)
    return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
}

/**
 * Allows responding with a relative redirect to a typed instance of a class annotated
 * with @Resource using the Resources plugin.
 */
suspend inline fun <reified T : Any> ApplicationCall.redirect(resource: T) {
    respondRedirect(application.href(resource))
}

/**
 * Generates a security code using a [hashFunction], a [date], a [user] and an implicit [HttpHeaders.Referrer]
 * to generate tokens to prevent CSRF attacks.
 */
fun ApplicationCall.securityCode(date: Long, user: User, hashFunction: (String) -> String) =
    hashFunction("$date:${user.userId}:${request.host()}:${refererHost()}")

/**
 * Verifies that a code generated from [securityCode] is valid for a [date] and a [user] and an implicit [HttpHeaders.Referrer].
 * It should match the generated [securityCode] and also not be older than two hours.
 * Used to prevent CSRF attacks.
 */
fun ApplicationCall.verifyCode(date: Long, user: User, code: String, hashFunction: (String) -> String) =
    securityCode(date, user, hashFunction) == code &&
        (System.currentTimeMillis() - date).let { it > 0 && it < TimeUnit.MILLISECONDS.convert(2, TimeUnit.HOURS) }

/**
 * Obtains the [refererHost] from the [HttpHeaders.Referrer] header, to check it to prevent CSRF attacks
 * from other domains.
 */
fun ApplicationCall.refererHost() = request.header(HttpHeaders.Referrer)?.let { URI.create(it).host }

/**
 * Pattern to validate an `userId`
 */
private val userIdPattern = "[a-zA-Z0-9_\\.]+".toRegex()

/**
 * Validates that an [userId] (that is also the user name) is a valid identifier.
 * Here we could add additional checks like the length of the user.
 * Or other things like a bad word filter.
 */
internal fun userNameValid(userId: String) = userId.matches(userIdPattern)
