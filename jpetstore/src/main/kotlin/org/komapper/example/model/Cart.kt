package org.komapper.example.model

import org.komapper.example.entity.Inventory
import org.komapper.example.entity.Item
import org.komapper.example.entity.Product
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component
import java.io.Serializable
import java.math.BigDecimal

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
class Cart : Serializable {
    private val itemMap: MutableMap<String, CartItem> = mutableMapOf()

    val cartItemList: List<CartItem>
        get() = itemMap.values.toList()

    fun getCartItem(itemId: String): CartItem? {
        return itemMap[itemId]
    }

    val numberOfItems: Int
        get() = itemMap.size

    fun containsItemId(itemId: String): Boolean {
        return itemMap.containsKey(itemId)
    }

    fun addItem(item: Item, product: Product, inventory: Inventory) {
        val current = itemMap[item.itemId]
        val new = current?.copy(quantity = current.quantity + 1)
            ?: CartItem(item, product, inventory)
        itemMap[item.itemId] = new
    }

    fun removeItemById(itemId: String) {
        itemMap.remove(itemId)
    }

    fun incrementQuantity(itemId: String) {
        val current = itemMap[itemId]
        if (current != null) {
            itemMap[itemId] = current.copy(quantity = current.quantity + 1)
        }
    }

    fun setQuantityByItemId(itemId: String, quantity: Int) {
        val current = itemMap[itemId]
        if (current != null) {
            itemMap[itemId] = current.copy(quantity = quantity)
        }
    }

    val total: BigDecimal
        get() = itemMap.values.fold(BigDecimal.ZERO) { sum, cartItem ->
            sum + cartItem.total
        }

    val isEmpty: Boolean
        get() = itemMap.isEmpty()

    fun clear() {
        itemMap.clear()
    }
}
