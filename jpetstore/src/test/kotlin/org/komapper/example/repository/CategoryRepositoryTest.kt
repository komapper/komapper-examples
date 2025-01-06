package org.komapper.example.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.komapper.example.entity.Category
import org.komapper.spring.boot.test.autoconfigure.jdbc.KomapperJdbcTest
import org.springframework.beans.factory.annotation.Autowired

@KomapperJdbcTest
class CategoryRepositoryTest(
    @Autowired
    private val categoryRepository: CategoryRepository,
) {
    @Test
    fun fetchCategory() {
        assertThat(categoryRepository.fetchCategory("BIRDS")).isEqualTo(
            Category(
                categoryId = "BIRDS",
                name = "Birds",
                description = """<image src="/images/birds_icon.gif"><font size="5" color="blue"> Birds</font>""",
            )
        )
    }
}
