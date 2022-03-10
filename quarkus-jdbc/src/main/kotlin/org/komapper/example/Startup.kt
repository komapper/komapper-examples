package org.komapper.example

import io.quarkus.runtime.StartupEvent
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.query.andThen
import org.komapper.jdbc.JdbcDatabase
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes
import javax.inject.Inject

@ApplicationScoped
class Startup @Inject constructor(private val database: JdbcDatabase) {

    fun startup(@Observes event: StartupEvent) {
        val q1 = QueryDsl.create(Meta.message)
        val q2 = QueryDsl.insert(Meta.message)
            .multiple(Message(text = "Hello"), Message(text = "World!"))
        database.runQuery(q1.andThen(q2))
    }
}
