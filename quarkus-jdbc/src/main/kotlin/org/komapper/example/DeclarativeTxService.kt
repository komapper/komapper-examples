package org.komapper.example

import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.operator.desc
import org.komapper.jdbc.JdbcDatabase
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam

@Path("/")
@Produces("application/json")
@Consumes("application/json")
@Transactional
@ApplicationScoped
class DeclarativeTxService @Inject constructor(private val database: JdbcDatabase) {

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
}
