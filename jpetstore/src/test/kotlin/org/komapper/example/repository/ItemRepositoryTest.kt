package org.komapper.example.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.komapper.example.entity.Inventory
import org.komapper.example.entity.Item
import org.komapper.example.entity.Product
import org.komapper.example.model.ItemAggregate
import org.komapper.spring.boot.test.autoconfigure.jdbc.KomapperJdbcTest
import org.springframework.beans.factory.annotation.Autowired

@KomapperJdbcTest
class ItemRepositoryTest(
    @Autowired
    private val itemRepository: ItemRepository,
) {
    @Test
    fun fetchItemAggregate() {
        assertThat(itemRepository.fetchItemAggregate("EST-1")).isEqualTo(
            ItemAggregate(
                Item(
                    itemId = "EST-1",
                    productId = "FI-SW-01",
                    listPrice = "16.50".toBigDecimal(),
                    unitCost = "10.00".toBigDecimal(),
                    supplierId = 1,
                    status = "P",
                    attribute1 = "Large",
                    attribute2 = null,
                    attribute3 = null,
                    attribute4 = null,
                    attribute5 = null,
                ),
                Inventory(
                    itemId = "EST-1",
                    quantity = 10000,
                ),
                Product(
                    productId = "FI-SW-01",
                    categoryId = "FISH",
                    name = "Angelfish",
                    description = """<image src="/images/fish1.gif">Salt Water fish from Australia""",
                )
            )
        )
    }
}
