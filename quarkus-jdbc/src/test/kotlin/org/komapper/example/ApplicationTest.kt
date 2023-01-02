package org.komapper.example

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Test

@QuarkusTest
class ApplicationTest {

    @Test
    fun declarative() {
        RestAssured.given()
            .`when`()["/"]
            .then()
            .statusCode(200)
            .body(
                CoreMatchers.containsString("Hello"),
                CoreMatchers.containsString("World!"),
            )
    }

    @Test
    fun imperative() {
        RestAssured.given()
            .`when`()["/imperative"]
            .then()
            .statusCode(200)
            .body(
                CoreMatchers.containsString("Hello"),
                CoreMatchers.containsString("World!"),
            )
    }
}
