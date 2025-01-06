package org.komapper.example.model

import org.komapper.example.entity.Item
import org.komapper.example.entity.Product

data class ProductAggregate(
    val product: Product,
    val itemSet: Set<Item>,
)
