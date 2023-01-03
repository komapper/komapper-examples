package io.ktor.samples.kweet

import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.formUrlEncode
import io.ktor.samples.kweet.dao.DAOFacade
import io.ktor.samples.kweet.model.Kweet
import io.ktor.samples.kweet.model.User
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.komapper.r2dbc.R2dbcDatabase
import org.komapper.tx.core.CoroutineTransactionOperator
import org.komapper.tx.core.TransactionAttribute
import org.komapper.tx.core.TransactionProperty
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Integration tests for the module [mainWithDependencies].
 *
 * Uses [testApp] in test methods to simplify the testing.
 */
class KweetApplicationTest {

    private val db = object : R2dbcDatabase by mockk(relaxed = true) {
        private val operator = mockk<CoroutineTransactionOperator>(relaxed = true)
        override suspend fun <R> withTransaction(
            transactionAttribute: TransactionAttribute,
            transactionProperty: TransactionProperty,
            block: suspend (CoroutineTransactionOperator) -> R,
        ): R {
            return block(operator)
        }
    }

    /**
     * A [mockk] instance of the [DAOFacade] to used to verify and mock calls on the integration tests.
     */
    private val dao = mockk<DAOFacade>(relaxed = true)

    /**
     * Specifies a fixed Date for testing.
     */
    private val date = LocalDateTime.of(2010, 1, 1, 0, 0, 0)

    /**
     * Tests that the [Index] page calls the [DAOFacade.top] method just once.
     * And that when no [Kweets] are available, it displays "There are no kweets yet" somewhere.
     */
    @Test
    fun testEmptyHome() = testApplication {
        setupApp()

        coEvery { dao.top() } returns listOf()

        client.get("/").apply {
            assertEquals(200, status.value)
            assertTrue(bodyAsText().contains("There are no kweets yet"))
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
    fun testHomeWithSomeKweets() = testApplication {
        setupApp()

        coEvery { dao.getKweet(1) } returns Kweet(1, "user1", "text1", date, null)
        coEvery { dao.getKweet(2) } returns Kweet(2, "user2", "text2", date, null)
        coEvery { dao.top() } returns listOf(1, 2)

        client.get("/").apply {
            assertEquals(200, status.value)
            assertFalse(bodyAsText().contains("There are no kweets yet"))
            assertTrue(bodyAsText().contains("user1"))
            assertTrue(bodyAsText().contains("user2"))
        }

        coVerify(exactly = 2) { dao.getKweet(any()) }
        coVerify(exactly = 1) { dao.top() }
    }

    /**
     * Verifies the behaviour of a login failure. That it should be a redirection to the /user page.
     */
    @Test
    fun testLoginFail() = testApplication {
        setupApp()

        client.post("/login") {
            header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            setBody(listOf("userId" to "myuser", "password" to "invalid").formUrlEncode())
        }.apply {
            assertEquals(302, status.value)
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
    fun testLoginSuccess() = testApplication {
        setupApp()
        val client = createClient {
            install(HttpCookies)
        }

        val password = "mylongpassword"
        val passwordHash = hash(password)
        coEvery { dao.user("test1", passwordHash) } returns User("test1", "test1@test.com", "test1", passwordHash)

        client.post("/login") {
            header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            setBody(listOf("userId" to "test1", "password" to password).formUrlEncode())
        }.apply {
            assertEquals(302, status.value)
            assertEquals("/user/test1", headers["Location"])
        }

        client.get("/").apply {
            assertEquals(200, status.value)
            assertTrue { bodyAsText().contains("sign out") }
        }
    }

    private fun ApplicationTestBuilder.setupApp() {
        application {
            mainWithDependencies(db, dao)
        }
        environment {
            config = MapApplicationConfig()
        }
    }
}
