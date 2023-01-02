package org.komapper.example.repository

import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.operator.asc
import org.komapper.example.entity.LineItem
import org.komapper.example.entity.Order
import org.komapper.example.entity.OrderStatus
import org.komapper.example.entity.item
import org.komapper.example.entity.lineItem
import org.komapper.example.entity.order
import org.komapper.example.entity.orderStatus
import org.komapper.example.entity.product
import org.komapper.example.model.OrderAggregate
import org.komapper.jdbc.JdbcDatabase
import org.springframework.stereotype.Repository

@Repository
class OrderRepository(private val db: JdbcDatabase) {

    private val o = Meta.order
    private val os = Meta.orderStatus
    private val li = Meta.lineItem
    private val item = Meta.item
    private val p = Meta.product

    fun fetchOrderAggregate(orderId: Int): OrderAggregate? {
        val query = QueryDsl
            .from(o)
            .innerJoin(li) { o.orderId eq li.orderId }
            .innerJoin(os) {
                o.orderId eq os.orderId
                li.lineNumber eq os.lineNumber
            }
            .leftJoin(item) { li.itemId eq item.itemId }
            .leftJoin(p) { item.productId eq p.productId }
            .where { o.orderId eq orderId }
            .includeAll()
        val store = db.runQuery(query)
        return store[o].firstOrNull()?.let {
            OrderAggregate(
                order = it,
                orderStatus = store[os].first(),
                lineItemSet = store[li],
                lineItem_product = store.oneToOne(li, p),
            )
        }
    }

    fun fetchOrderList(username: String): List<Order> {
        val query = QueryDsl.from(o)
            .where { o.username eq username }
            .orderBy(o.orderDate.asc())
        return db.runQuery(query)
    }

    fun insertOrder(order: Order): Order {
        val query = QueryDsl.insert(o).single(order)
        return db.runQuery(query)
    }

    fun insertLineItemList(lineItemList: List<LineItem>): List<LineItem> {
        val query = QueryDsl.insert(li).multiple(lineItemList)
        return db.runQuery(query)
    }

    fun insertOrderStatusList(orderStatusList: List<OrderStatus>): List<OrderStatus> {
        val query = QueryDsl.insert(os).multiple(orderStatusList)
        return db.runQuery(query)
    }
}
