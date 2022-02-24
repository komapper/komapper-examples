package org.komapper.example

import kotlinx.coroutines.flow.Flow
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.r2dbc.R2dbcDatabase
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@RestController
@Transactional
class Application(private val database: R2dbcDatabase) {

    @RequestMapping("/")
    suspend fun list(): Flow<Message> {
        return database.flowQuery {
            val m = Meta.message
            QueryDsl.from(m).orderBy(m.id)
        }
    }

    @RequestMapping(value = ["/"], params = ["text"])
    suspend fun add(@RequestParam text: String): Message {
        val message = Message(text = text)
        return database.runQuery {
            val m = Meta.message
            QueryDsl.insert(m).single(message)
        }
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
