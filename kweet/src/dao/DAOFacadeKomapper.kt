package io.ktor.samples.kweet.dao

import io.ktor.samples.kweet.model.Kweet
import io.ktor.samples.kweet.model.User
import kotlinx.coroutines.runBlocking
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.operator.count
import org.komapper.core.dsl.operator.desc
import org.komapper.core.dsl.query.first
import org.komapper.core.dsl.query.firstOrNull
import org.komapper.core.dsl.query.map
import org.komapper.r2dbc.R2dbcDatabase
import java.time.LocalDateTime

class DAOFacadeKomapper(
    private val db: R2dbcDatabase,
) : DAOFacade {

    private val k = Meta.kweet
    private val k2 = Meta.kweet2
    private val u = Meta.user

    override fun init() = runBlocking {
        db.runQuery {
            QueryDsl.create(k, u)
        }
    }

    override suspend fun countReplies(id: Int): Int {
        val query = QueryDsl.from(k).where { k.replyTo eq id }.selectNotNull(count(k.id))
        return db.runQuery(query).toInt()
    }

    override suspend fun createKweet(user: String, text: String, replyTo: Int?, date: LocalDateTime): Int {
        val kweet = Kweet(0, user, text, date, replyTo)
        val query = QueryDsl.insert(k).single(kweet).map { it.id }
        return db.runQuery(query)
    }

    override suspend fun deleteKweet(id: Int) {
        val query = QueryDsl.delete(k).where { k.id eq id }
        db.runQuery(query)
    }

    override suspend fun getKweet(id: Int): Kweet {
        val query = QueryDsl.from(k).where { k.id eq id }.first()
        return db.runQuery(query)
    }

    override suspend fun userKweets(userId: String): List<Kweet> {
        val query = QueryDsl.from(k).where { k.userId eq userId }
            .orderBy(k.date.desc())
            .limit(100)
        return db.runQuery(query)
    }

    override suspend fun user(userId: String, hash: String?): User? {
        val query = QueryDsl.from(u).where { u.userId eq userId }
        val users = db.runQuery(query)
        return users
            .mapNotNull {
                if (hash == null || it.passwordHash == hash) {
                    it
                } else {
                    null
                }
            }
            .singleOrNull()
    }

    override suspend fun userByEmail(email: String): User? {
        val query = QueryDsl.from(u).where { u.email eq email }.firstOrNull()
        return db.runQuery(query)
    }

    override suspend fun createUser(user: User) {
        val query = QueryDsl.insert(u).single(user)
        db.runQuery(query)
        Unit
    }

    override suspend fun top(count: Int): List<Int> {
        // note: in a real application you shouldn't do it like this
        //   as it may cause database outages on big data
        //   so this implementation is just for demo purposes
        val query = QueryDsl.from(k)
            .leftJoin(k2) { k.id eq k2.replyTo }
            .orderBy(count(k2.id).desc())
            .limit(count)
            .groupBy(k.id)
            .selectNotNull(k.id)
        return db.runQuery(query)
    }

    override fun close() {
    }
}
