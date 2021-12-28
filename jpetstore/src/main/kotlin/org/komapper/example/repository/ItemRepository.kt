package org.komapper.example.repository

import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.jdbc.JdbcDatabase
import org.springframework.stereotype.Repository
import org.komapper.example.entity.inventory
import org.komapper.example.entity.item
import org.komapper.example.entity.product
import org.komapper.example.model.ItemAggregate

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
        return store.list(item).firstOrNull()?.let {
            ItemAggregate(
                item = it,
                inventory = store.list(inv).single(),
                product = store.list(p).single()
            )
        }
    }
}
