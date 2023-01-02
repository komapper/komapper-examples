package org.komapper.example.service

import org.komapper.example.entity.LineItem
import org.komapper.example.entity.Order
import org.komapper.example.entity.OrderStatus
import org.komapper.example.model.Cart
import org.komapper.example.model.OrderAggregate
import org.komapper.example.repository.AccountRepository
import org.komapper.example.repository.OrderRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val accountRepository: AccountRepository,
) {

    fun executeOrder(username: String, cart: Cart) {
        val order = orderRepository.insertOrder(createOrder(username, cart))
        val lineItemList = orderRepository.insertLineItemList(createLineItemList(order, cart))
        orderRepository.insertOrderStatusList(createOrderStatusList(order, lineItemList))
    }

    fun getOrderAggregate(orderId: Int): OrderAggregate? {
        return orderRepository.fetchOrderAggregate(orderId)
    }

    fun getOrderList(username: String): List<Order> {
        return orderRepository.fetchOrderList(username)
    }

    fun createOrder(username: String, cart: Cart): Order {
        val accountAggregate = accountRepository.fetchAccountAggregate(username)
        val account = accountAggregate.account
        return Order(
            username = username,
            orderDate = LocalDateTime.now(),
            shipToFirstName = account.firstName,
            shipToLastName = account.lastName,
            shipAddress1 = account.address1,
            shipAddress2 = account.address2,
            shipCity = account.city,
            shipState = account.state,
            shipZip = account.zip,
            shipCountry = account.country,
            billToFirstName = account.firstName,
            billToLastName = account.lastName,
            billAddress1 = account.address1,
            billAddress2 = account.address2,
            billCity = account.city,
            billState = account.state,
            billZip = account.zip,
            billCountry = account.country,
            totalPrice = cart.total,
            creditCard = "999 9999 9999 9999",
            expiryDate = "12/03",
            cardType = "Visa",
            courier = "UPS",
            locale = "CA",
        )
    }

    private fun createLineItemList(order: Order, cart: Cart): List<LineItem> {
        return cart.cartItemList.mapIndexed { index, cartItem ->
            LineItem(
                orderId = order.orderId,
                lineNumber = index + 1,
                quantity = cartItem.quantity,
                itemId = cartItem.item.itemId,
                unitPrice = cartItem.item.listPrice,
            )
        }
    }

    private fun createOrderStatusList(order: Order, lineItemList: List<LineItem>): List<OrderStatus> {
        return lineItemList.map {
            OrderStatus(
                orderId = order.orderId,
                lineNumber = it.lineNumber,
                timestamp = order.orderDate.toLocalDate(),
                status = "P",
            )
        }
    }
}
