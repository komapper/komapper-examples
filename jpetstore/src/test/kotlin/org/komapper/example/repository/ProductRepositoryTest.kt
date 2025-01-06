package org.komapper.example.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.komapper.example.entity.Item
import org.komapper.example.entity.Product
import org.komapper.example.model.ProductAggregate
import org.komapper.spring.boot.test.autoconfigure.jdbc.KomapperJdbcTest
import org.springframework.beans.factory.annotation.Autowired

@KomapperJdbcTest
class ProductRepositoryTest(
    @Autowired
    private val productRepository: ProductRepository,
) {
    @Test
    fun fetchProductListByCategoryId() {
        assertThat(productRepository.fetchProductListByCategoryId("FISH")).containsExactlyInAnyOrder(
            Product(
                productId = "FI-FW-01",
                categoryId = "FISH",
                name = "Koi",
                description = """<image src="/images/fish3.gif">Fresh Water fish from Japan""",
            ),
            Product(
                productId = "FI-FW-02",
                categoryId = "FISH",
                name = "Goldfish",
                description = """<image src="/images/fish2.gif">Fresh Water fish from China""",
            ),
            Product(
                productId = "FI-SW-01",
                categoryId = "FISH",
                name = "Angelfish",
                description = """<image src="/images/fish1.gif">Salt Water fish from Australia""",
            ),
            Product(
                productId = "FI-SW-02",
                categoryId = "FISH",
                name = "Tiger Shark",
                description = """<image src="/images/fish4.gif">Salt Water fish from Australia""",
            ),
        )
    }

    @Test
    fun fetchProductAggregate() {
        assertThat(productRepository.fetchProductAggregate("FI-FW-01")).isEqualTo(
            ProductAggregate(
                Product(
                    productId = "FI-FW-01",
                    name = "Koi",
                    categoryId = "FISH",
                    description = """<image src="/images/fish3.gif">Fresh Water fish from Japan""",
                ),
                setOf(
                    Item(
                        itemId = "EST-4",
                        productId = "FI-FW-01",
                        listPrice = "18.50".toBigDecimal(),
                        unitCost = "12.00".toBigDecimal(),
                        supplierId = 1,
                        status = "P",
                        attribute1 = "Spotted",
                        attribute2 = null,
                        attribute3 = null,
                        attribute4 = null,
                        attribute5 = null,
                    ),
                    Item(
                        itemId = "EST-5",
                        productId = "FI-FW-01",
                        listPrice = "18.50".toBigDecimal(),
                        unitCost = "12.00".toBigDecimal(),
                        supplierId = 1,
                        status = "P",
                        attribute1 = "Spotless",
                        attribute2 = null,
                        attribute3 = null,
                        attribute4 = null,
                        attribute5 = null,
                    ),
                ),
            )
        )
    }

    @Test
    fun fetchProductListByKeywords() {
        val products = productRepository.fetchProductListByKeywords(listOf("ol"))
        assertThat(products.map { it.name })
            .containsExactlyInAnyOrder(
                "Goldfish",
                "Golden Retriever",
            )
    }
}
