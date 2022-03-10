package io.ktor.samples.kweet

import io.ktor.http.ContentType
import io.ktor.http.Cookie
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.encodeURLParameter
import io.ktor.http.formUrlEncode
import io.ktor.samples.kweet.dao.DAOFacade
import io.ktor.samples.kweet.model.Kweet
import io.ktor.samples.kweet.model.User
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.komapper.r2dbc.R2dbcDatabase
import java.time.LocalDateTime
import kotlin.test.Ignore
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Integration tests for the module [mainWithDependencies].
 *
 * Uses [testApp] in test methods to simplify the testing.
 */
class KweetApplicationTest {

    val db = mockk<R2dbcDatabase>(relaxed = true)

    /**
     * A [mockk] instance of the [DAOFacade] to used to verify and mock calls on the integration tests.
     */
    val dao = mockk<DAOFacade>(relaxed = true)

    /**
     * Specifies a fixed Date for testing.
     */
    val date = LocalDateTime.of(2010, 1, 1, 0, 0, 0)

    /**
     * Tests that the [Index] page calls the [DAOFacade.top] method just once.
     * And that when no [Kweets] are available, it displays "There are no kweets yet" somewhere.
     */
    @Test
    fun testEmptyHome() = testApp {
        coEvery { dao.top() } returns listOf()

        handleRequest(HttpMethod.Get, "/").apply {
            assertEquals(200, response.status()?.value)
            assertTrue(response.content!!.contains("There are no kweets yet"))
        }

        coVerify(exactly = 1) { dao.top() }
    }

    /**
     * Tests that the [Index] page calls the [DAOFacade.top] method just once.
     * And that when some Kweets are available there is a call to [DAOFacade.getKweet] per provided kweet id.
     * Ensures that it DOESN'T display "There are no kweets yet" when there are kweets available,
     * and that the user of the kweets is also displayed.
     */
    @Test
    fun testHomeWithSomeKweets() = testApp {
        coEvery { dao.getKweet(1) } returns Kweet(1, "user1", "text1", date, null)
        coEvery { dao.getKweet(2) } returns Kweet(2, "user2", "text2", date, null)
        coEvery { dao.top() } returns listOf(1, 2)

        handleRequest(HttpMethod.Get, "/").apply {
            assertEquals(200, response.status()?.value)
            assertFalse(response.content!!.contains("There are no kweets yet"))
            assertTrue(response.content!!.contains("user1"))
            assertTrue(response.content!!.contains("user2"))
        }

        coVerify(exactly = 2) { dao.getKweet(any()) }
        coVerify(exactly = 1) { dao.top() }
    }

    /**
     * Verifies the behaviour of a login failure. That it should be a redirection to the /user page.
     */
    @Test
    @Ignore
    fun testLoginFail() = testApp {
        handleRequest(HttpMethod.Post, "/login") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            setBody(listOf("userId" to "myuser", "password" to "invalid").formUrlEncode())
        }.apply {
            assertEquals(302, response.status()?.value)
            assertEquals("http://localhost/user", response.headers["Location"])
        }
    }

    /**
     * Verifies a chain of requests verifying the [Login].
     * It mocks a get [DAOFacade.user] request, checks that posting valid credentials to the /login form
     * redirects to the user [UserPage] for that user, and reuses the returned cookie for a request
     * to the [UserPage] and verifies that with that cookie/session, there is a "sign out" text meaning that
     * the user is logged in.
     */
    @Test
    @Ignore
    fun testLoginSuccess() = testApp {
        val password = "mylongpassword"
        val passwordHash = hash(password)
        val sessionCookieName = "SESSION"
        lateinit var sessionCookie: Cookie
        coEvery { dao.user("test1", passwordHash) } returns User("test1", "test1@test.com", "test1", passwordHash)

        handleRequest(HttpMethod.Post, "/login") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            setBody(listOf("userId" to "test1", "password" to password).formUrlEncode())
        }.apply {
            assertEquals(302, response.status()?.value)
            assertEquals("http://localhost/user/test1", response.headers["Location"])
            assertEquals(null, response.content)
            sessionCookie = response.cookies[sessionCookieName]!!
        }

        handleRequest(HttpMethod.Get, "/") {
            addHeader(HttpHeaders.Cookie, "$sessionCookieName=${sessionCookie.value.encodeURLParameter()}")
        }.apply {
            assertTrue { response.content!!.contains("sign out") }
        }
    }

    /**
     * Private method used to reduce boilerplate when testing the application.
     */
    private fun testApp(callback: TestApplicationEngine.() -> Unit) {
        withTestApplication({ mainWithDependencies(db, dao) }) { runBlocking { callback() } }
    }
}
