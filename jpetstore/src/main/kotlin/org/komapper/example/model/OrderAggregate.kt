package org.komapper.example.model

import org.komapper.example.entity.LineItem
import org.komapper.example.entity.Order
import org.komapper.example.entity.OrderStatus
import org.komapper.example.entity.Product

class OrderAggregate(
    val order: Order,
    val orderStatus: OrderStatus,
    val lineItemList: List<LineItem>,
    val lineItem_product: Map<LineItem, Product?>,
)
