package org.komapper.example

import io.quarkus.runtime.StartupEvent
import org.komapper.annotation.KomapperAutoIncrement
import org.komapper.annotation.KomapperEntityDef
import org.komapper.annotation.KomapperId
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.operator.desc
import org.komapper.core.dsl.query.andThen
import org.komapper.jdbc.JdbcDatabase
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes
import javax.inject.Inject
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam

@Path("/")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
class Application @Inject constructor(private val database: JdbcDatabase) {

    @GET
    fun list(): List<Message> {
        return database.runQuery {
            val m = Meta.message
            QueryDsl.from(m).orderBy(m.id.desc())
        }
    }

    @GET
    @Path("/add")
    fun add(@QueryParam("text") text: String?): Message {
        val message = Message(text = text ?: "empty")
        return database.runQuery {
            val m = Meta.message
            QueryDsl.insert(m).single(message)
        }
    }

    fun startup(@Observes event: StartupEvent) {
        val q1 = QueryDsl.create(Meta.message)
        val q2 = QueryDsl.insert(Meta.message)
            .multiple(Message(text = "Hello"), Message(text = "World!"))
        database.runQuery(q1.andThen(q2))
    }
}

data class Message(
    val id: Int? = null,
    val text: String
)

@KomapperEntityDef(Message::class)
data class MessageDef(
    @KomapperId @KomapperAutoIncrement val id: Nothing,
)
