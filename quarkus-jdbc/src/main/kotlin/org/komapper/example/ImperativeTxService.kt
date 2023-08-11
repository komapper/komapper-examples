package org.komapper.example

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.operator.desc
import org.komapper.jdbc.JdbcDatabase

@Path("/imperative")
@Produces("application/json")
@Consumes("application/json")
@ApplicationScoped
class ImperativeTxService @Inject constructor(private val database: JdbcDatabase) {

    @GET
    fun list(): List<Message> = database.withTransaction {
        database.runQuery {
            val m = Meta.message
            QueryDsl.from(m).orderBy(m.id.desc())
        }
    }

    @GET
    @Path("/add")
    fun add(@QueryParam("text") text: String?): Message = database.withTransaction {
        val message = Message(text = text ?: "empty")
        database.runQuery {
            val m = Meta.message
            QueryDsl.insert(m).single(message)
        }
    }
}
