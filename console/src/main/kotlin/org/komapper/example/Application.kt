package org.komapper.example

import org.komapper.core.Database
import org.komapper.core.dsl.EntityDsl
import org.komapper.core.dsl.SchemaDsl
import org.komapper.core.dsl.runQuery
import org.komapper.transaction.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val logger: Logger = LoggerFactory.getLogger("console")

fun main() {
    // create a Database instance
    val db = Database.create("jdbc:h2:mem:example-console;DB_CLOSE_DELAY=-1")

    // get a metamodel
    val a = AddressDef.meta

    // execute simple CRUD operations in a transaction
    db.transaction {
        // create a schema
        db.runQuery {
            SchemaDsl.create(a)
        }

        // INSERT
        val newAddress = db.runQuery {
            EntityDsl.insert(a).single(Address(street = "street A"))
        }

        // SELECT
        val address1 = db.runQuery {
            EntityDsl.from(a).first { a.id eq newAddress.id }
        }

        logger.info("address1 = $address1")

        // UPDATE
        db.runQuery {
            EntityDsl.update(a).single(address1.copy(street = "street B"))
        }

        // SELECT
        val address2 = db.runQuery {
            EntityDsl.from(a).first { a.street eq "street B" }
        }

        logger.info("address2 = $address2")
        check(address1.id == address2.id)
        check(address1.street != address2.street)
        check(address1.version + 1 == address2.version)

        // DELETE
        db.runQuery {
            EntityDsl.delete(a).single(address2)
        }

        // SELECT
        val addressList = db.runQuery {
            EntityDsl.from(a).orderBy(a.id)
        }

        logger.info("addressList = $addressList")
        check(addressList.isEmpty()) { "The addressList must be empty." }
    }
}
