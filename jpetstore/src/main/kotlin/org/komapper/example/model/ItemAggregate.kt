package org.komapper.example.model

import org.komapper.example.entity.Inventory
import org.komapper.example.entity.Item
import org.komapper.example.entity.Product

data class ItemAggregate(
    val item: Item,
    val inventory: Inventory,
    val product: Product,
)
