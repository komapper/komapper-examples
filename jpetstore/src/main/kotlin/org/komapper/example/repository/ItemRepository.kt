package org.komapper.example.repository

import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.example.entity.inventory
import org.komapper.example.entity.item
import org.komapper.example.entity.product
import org.komapper.example.model.ItemAggregate
import org.komapper.jdbc.JdbcDatabase
import org.springframework.stereotype.Repository

@Repository
class ItemRepository(private val db: JdbcDatabase) {

    private val item = Meta.item
    private val inv = Meta.inventory
    private val p = Meta.product

    fun fetchItemAggregate(itemId: String): ItemAggregate? {
        val query = QueryDsl
            .from(item)
            .innerJoin(inv) { item.itemId eq inv.itemId }
            .innerJoin(p) { item.productId eq p.productId }
            .where { item.itemId eq itemId }
            .includeAll()
        val store = db.runQuery(query)
        return store[item].firstOrNull()?.let {
            ItemAggregate(
                item = it,
                inventory = store[inv].single(),
                product = store[p].single(),
            )
        }
    }
}
