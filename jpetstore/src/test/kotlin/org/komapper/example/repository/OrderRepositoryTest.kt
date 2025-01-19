package org.komapper.example.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.example.entity.LineItem
import org.komapper.example.entity.Order
import org.komapper.example.entity.OrderStatus
import org.komapper.example.entity.Product
import org.komapper.example.entity.order
import org.komapper.example.entity.orderStatus
import org.komapper.example.model.OrderAggregate
import org.komapper.jdbc.JdbcDatabase
import org.komapper.spring.boot.test.autoconfigure.jdbc.KomapperJdbcTest
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@KomapperJdbcTest
class OrderRepositoryTest(
    @Autowired
    private val orderRepository: OrderRepository,
    @Autowired
    private val db: JdbcDatabase,
) {
    @Test
    fun insertOrder() {
        val order = Order(
            orderDate = LocalDateTime.of(2018, 12, 31, 23, 59, 59),
            username = "komapper",
            cardType = "Visa",
            creditCard = "1234 5678 9012 3456",
            expiryDate = "06/2022",
            courier = "Courier",
            locale = "ja",
            totalPrice = "2000.05".toBigDecimal(),
            billAddress1 = "Bill Address1",
            billAddress2 = "Bill Address2",
            billCity = "Bill City",
            billState = "Bill State",
            billCountry = "USA",
            billZip = "80001",
            billToFirstName = "Bill First Name",
            billToLastName = "Bill Last Name",
            shipAddress1 = "Ship Address1",
            shipAddress2 = "Ship Address2",
            shipCity = "Ship City",
            shipState = "Ship State",
            shipCountry = "JPN",
            shipZip = "70001",
            shipToFirstName = "Ship First Name",
            shipToLastName = "Ship Last Name",
        )

        val orderId = orderRepository.insertOrder(order).orderId

        val query = QueryDsl.from(Meta.order).where { Meta.order.orderId eq orderId }
        assertThat(db.runQuery(query)).containsExactly(order.copy(orderId = orderId))
    }

    @Test
    fun insertOrderStatusList() {
        val orderStatus = OrderStatus(
            orderId = 1,
            lineNumber = 1,
            timestamp = LocalDate.of(2018, 12, 31),
            status = "OK",
        )

        orderRepository.insertOrderStatusList(listOf(orderStatus))

        val query = QueryDsl.from(Meta.orderStatus).where { Meta.orderStatus.orderId eq 1 }
        assertThat(db.runQuery(query)).containsExactly(orderStatus)
    }

    @Test
    fun fetchOrderList() {
        val newOrder = Order(
            orderDate = LocalDateTime.of(2018, 12, 31, 23, 59, 59),
            username = "komapper",
            cardType = "Visa",
            creditCard = "1234 5678 9012 3456",
            expiryDate = "06/2022",
            courier = "Courier",
            locale = "ja",
            totalPrice = "2000.05".toBigDecimal(),
            billAddress1 = "Bill Address1",
            billAddress2 = "Bill Address2",
            billCity = "Bill City",
            billState = "Bill State",
            billCountry = "USA",
            billZip = "80001",
            billToFirstName = "Bill First Name",
            billToLastName = "Bill Last Name",
            shipAddress1 = "Ship Address1",
            shipAddress2 = "Ship Address2",
            shipCity = "Ship City",
            shipState = "Ship State",
            shipCountry = "JPN",
            shipZip = "70001",
            shipToFirstName = "Ship First Name",
            shipToLastName = "Ship Last Name"
        )
        val orderId = orderRepository.insertOrder(newOrder).orderId

        assertThat(orderRepository.fetchOrderList("komapper")).containsExactly(newOrder.copy(orderId = orderId))
    }

    @Test
    fun fetchOrderAggregate() {
        val order = Order(
            orderDate = LocalDateTime.of(2018, 12, 31, 23, 59, 59),
            username = "j2ee",
            cardType = "Visa",
            creditCard = "1234 5678 9012 3456",
            expiryDate = "06/2022",
            courier = "Courier",
            locale = "ja",
            totalPrice = BigDecimal("2000.05"),
            billAddress1 = "Bill Address1",
            billAddress2 = "Bill Address2",
            billCity = "Bill City",
            billState = "Bill State",
            billCountry = "USA",
            billZip = "80001",
            billToFirstName = "Bill First Name",
            billToLastName = "Bill Last Name",
            shipAddress1 = "Ship Address1",
            shipAddress2 = "Ship Address2",
            shipCity = "Ship City",
            shipState = "Ship State",
            shipCountry = "JPN",
            shipZip = "70001",
            shipToFirstName = "Ship First Name",
            shipToLastName = "Ship Last Name",
        )
        val orderId = orderRepository.insertOrder(order).orderId

        val orderLine = LineItem(
            orderId = orderId,
            lineNumber = 1,
            itemId = "EST-7",
            quantity = 2,
            unitPrice = "14.00".toBigDecimal(),
        )
        orderRepository.insertLineItemList(listOf(orderLine))

        val orderStatus = OrderStatus(
            orderId = orderId,
            lineNumber = 1,
            timestamp = LocalDate.of(2018, 12, 31),
            status = "OK",
        )
        orderRepository.insertOrderStatusList(listOf(orderStatus))

        val orderAggregate = orderRepository.fetchOrderAggregate(orderId)
        assertThat(orderAggregate)
            .isNotNull()
            .isEqualTo(
                OrderAggregate(
                    order = order.copy(orderId = orderId),
                    orderStatus = orderStatus,
                    lineItemSet = setOf(orderLine),
                    lineItem_product = mapOf(
                        orderLine to Product(
                            productId = "K9-BD-01",
                            categoryId = "DOGS",
                            name = "Bulldog",
                            description = """<image src="/images/dog2.gif">Friendly dog from England""",
                        ),
                    ),
                )
            )
    }
}
