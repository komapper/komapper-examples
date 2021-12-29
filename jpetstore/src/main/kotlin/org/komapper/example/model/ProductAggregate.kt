package org.komapper.example.model

import org.komapper.example.entity.Item
import org.komapper.example.entity.Product

class ProductAggregate(
    val product: Product,
    val itemList: List<Item>,
)
