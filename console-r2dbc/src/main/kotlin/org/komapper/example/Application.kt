package org.komapper.example

import kotlinx.coroutines.flow.toList
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.query.first
import org.komapper.r2dbc.R2dbcDatabase
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val logger: Logger = LoggerFactory.getLogger("console")

suspend fun main() {
    // create a Database instance
    val db = R2dbcDatabase("r2dbc:h2:mem:///example;DB_CLOSE_DELAY=-1")

    // get a metamodel
    val a = Meta.address

    // execute simple CRUD operations in a transaction
    db.withTransaction {
        // create a schema
        db.runQuery {
            QueryDsl.create(a)
        }

        // INSERT
        val newAddress = db.runQuery {
            QueryDsl.insert(a).single(Address(street = "street A"))
        }

        // SELECT
        val address1 = db.runQuery {
            QueryDsl.from(a).where { a.id eq newAddress.id }.first()
        }

        logger.info("address1 = $address1")

        // UPDATE
        db.runQuery {
            QueryDsl.update(a).single(address1.copy(street = "street B"))
        }

        // SELECT
        val address2 = db.runQuery {
            QueryDsl.from(a).where { a.street eq "street B" }.first()
        }

        logger.info("address2 = $address2")
        check(address1.id == address2.id)
        check(address1.street != address2.street)
        check(address1.version + 1 == address2.version)

        // SELECT ALL as Flow
        val flow = db.flowQuery {
            QueryDsl.from(a)
        }
        val list = flow.toList()
        check(1 == list.size)

        // DELETE
        db.runQuery {
            QueryDsl.delete(a).single(address2)
        }

        // SELECT
        val addressList = db.runQuery {
            QueryDsl.from(a).orderBy(a.id)
        }

        logger.info("addressList = $addressList")
        check(addressList.isEmpty()) { "The addressList must be empty." }
    }
}
