package org.komapper.example

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Test

@QuarkusTest
class ApplicationTest {

    @Test
    fun list() {
        RestAssured.given()
            .`when`()["/"]
            .then()
            .statusCode(200)
            .body(
                CoreMatchers.containsString("Hello"),
                CoreMatchers.containsString("World!")
            )
    }
}
