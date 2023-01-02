package org.komapper.example

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class ApplicationTest(
    @Autowired private val restTemplate: TestRestTemplate,
    @LocalServerPort private val port: Int,
) {

    @Test
    fun fish() {
        Assertions.assertThat(
            restTemplate.getForObject("http://localhost:$port/category/FISH", String::class.java),
        ).contains("Angelfish")
    }
}
