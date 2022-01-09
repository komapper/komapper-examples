package org.komapper.example.repository

import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.query.firstOrNull
import org.komapper.example.entity.Account
import org.komapper.example.entity.Profile
import org.komapper.example.entity.SignOn
import org.komapper.example.entity.account
import org.komapper.example.entity.bannerData
import org.komapper.example.entity.profile
import org.komapper.example.entity.signOn
import org.komapper.example.model.AccountAggregate
import org.komapper.jdbc.JdbcDatabase
import org.springframework.stereotype.Repository

@Repository
class AccountRepository(private val db: JdbcDatabase) {

    private val a = Meta.account
    private val p = Meta.profile
    private val s = Meta.signOn
    private val b = Meta.bannerData

    fun fetchSignOn(username: String): SignOn? {
        val query = QueryDsl.from(s).where { s.username eq username }.firstOrNull()
        return db.runQuery(query)
    }

    fun fetchAccountAggregate(username: String): AccountAggregate {
        val query = QueryDsl
            .from(a)
            .innerJoin(p) { a.username eq p.username }
            .innerJoin(s) { a.username eq s.username }
            .innerJoin(b) { p.favouriteCategoryId eq b.favouriteCategoryId }
            .where { a.username eq username }
            .includeAll()
        val store = db.runQuery(query)
        return AccountAggregate(
            account = store[a].single(),
            profile = store[p].single(),
            signOn = store[s].single(),
        )
    }

    fun insertAccount(account: Account) {
        val query = QueryDsl.insert(a).single(account)
        db.runQuery(query)
    }

    fun insertProfile(profile: Profile) {
        val query = QueryDsl.insert(p).single(profile)
        db.runQuery(query)
    }

    fun insertSignOn(signOn: SignOn) {
        val query = QueryDsl.insert(s).single(signOn)
        db.runQuery(query)
    }

    fun updateAccount(account: Account) {
        val query = QueryDsl.update(a).single(account)
        db.runQuery(query)
    }

    fun updateProfile(profile: Profile) {
        val query = QueryDsl.update(p).single(profile)
        db.runQuery(query)
    }
}
