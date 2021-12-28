package org.komapper.example.repository

import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.query.firstOrNull
import org.komapper.example.entity.Category
import org.komapper.example.entity.category
import org.komapper.jdbc.JdbcDatabase
import org.springframework.stereotype.Repository

@Repository
class CategoryRepository(private val db: JdbcDatabase) {

    private val c = Meta.category

    fun fetchCategory(categoryId: String): Category? {
        val query = QueryDsl.from(c).where { c.categoryId eq categoryId }.firstOrNull()
        return db.runQuery(query)
    }
}
