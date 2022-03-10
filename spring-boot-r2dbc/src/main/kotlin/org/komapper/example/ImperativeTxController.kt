package org.komapper.example

import kotlinx.coroutines.flow.Flow
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.r2dbc.R2dbcDatabase
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/imperative")
@RestController
@Transactional
class ImperativeTxController(private val database: R2dbcDatabase) {

    @RequestMapping
    suspend fun list(): Flow<Message> = database.withTransaction {
        database.flowQuery {
            val m = Meta.message
            QueryDsl.from(m).orderBy(m.id)
        }
    }

    @RequestMapping(params = ["text"])
    suspend fun add(@RequestParam text: String): Message = database.withTransaction {
        val message = Message(text = text)
        database.runQuery {
            val m = Meta.message
            QueryDsl.insert(m).single(message)
        }
    }
}
