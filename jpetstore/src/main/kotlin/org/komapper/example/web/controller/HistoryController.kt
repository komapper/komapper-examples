package org.komapper.example.web.controller

import org.komapper.example.service.OrderService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.server.ResponseStatusException

@Controller
@RequestMapping("/history")
@Transactional
class HistoryController(private val orderService: OrderService) {

    private val logger = LoggerFactory.getLogger(HistoryController::class.java)

    @GetMapping
    fun viewList(model: Model, @AuthenticationPrincipal user: User): String {
        val orderList = orderService.getOrderList(user.username)
        model.addAttribute("orderList", orderList)
        return "history/list"
    }

    @GetMapping("{orderId}")
    fun viewDetail(@PathVariable orderId: Int, model: Model): String {
        val orderAggregate = orderService.getOrderAggregate(orderId)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "orderId=$orderId not found")
        model.addAttribute("order", orderAggregate.order)
        model.addAttribute("orderStatus", orderAggregate.orderStatus)
        model.addAttribute("lineItemList", orderAggregate.lineItemSet)
        model.addAttribute("lineItem_product", orderAggregate.lineItem_product)
        return "history/detail"
    }
}
