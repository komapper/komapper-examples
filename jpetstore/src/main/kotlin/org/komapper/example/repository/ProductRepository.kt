package org.komapper.example.repository

import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.operator.lower
import org.komapper.example.entity.Product
import org.komapper.example.entity.item
import org.komapper.example.entity.product
import org.komapper.example.model.ProductAggregate
import org.komapper.jdbc.JdbcDatabase
import org.springframework.stereotype.Repository

@Repository
class ProductRepository(private val db: JdbcDatabase) {

    private val p = Meta.product
    private val item = Meta.item

    fun fetchProductListByCategoryId(categoryId: String?): List<Product> {
        val query = QueryDsl.from(p).where { p.categoryId eq categoryId }
        return db.runQuery(query)
    }

    fun fetchProductListByKeywords(keywords: List<String>): List<Product> {
        val query = QueryDsl.from(p).where {
            for (keyword in keywords) {
                or { lower(p.name) contains keyword.lowercase() }
                or { lower(p.categoryId) contains keyword.lowercase() }
                or { lower(p.description) contains keyword.lowercase() }
            }
        }
        return db.runQuery(query)
    }

    fun fetchProductAggregate(productId: String): ProductAggregate? {
        val query = QueryDsl
            .from(p)
            .innerJoin(item) { p.productId eq item.productId }
            .where { p.productId eq productId }
            .includeAll()
        val store = db.runQuery(query)
        return store[p].firstOrNull()?.let {
            ProductAggregate(
                product = it,
                itemSet = store[item],
            )
        }
    }
}
