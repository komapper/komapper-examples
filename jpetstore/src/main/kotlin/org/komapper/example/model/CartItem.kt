package org.komapper.example.model

import org.komapper.example.entity.Inventory
import org.komapper.example.entity.Item
import org.komapper.example.entity.Product
import java.io.Serializable
import java.math.BigDecimal

data class CartItem(
    val item: Item,
    val product: Product,
    val inventory: Inventory,
    val quantity: Int = 1,
) : Serializable {
    val total: BigDecimal = item.listPrice * BigDecimal(quantity)
    val isInStock: Boolean = inventory.quantity > 0
}
