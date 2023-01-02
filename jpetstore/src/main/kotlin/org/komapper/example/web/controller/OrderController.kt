package org.komapper.example.web.controller

import org.komapper.example.model.Cart
import org.komapper.example.service.OrderService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/order")
@Transactional
class OrderController(private val orderService: OrderService, private val cart: Cart) {
    @GetMapping
    fun confirm(
        model: Model,
        @AuthenticationPrincipal user: User,
        redirectAttributes: RedirectAttributes,
    ): String {
        if (cart.isEmpty) {
            return fillMessageAndRedirectToIndex(redirectAttributes)
        }
        val order = orderService.createOrder(user.username, cart)
        model.addAttribute("order", order)
        return "order/confirm"
    }

    @PostMapping
    fun confirm(@AuthenticationPrincipal user: User, redirectAttributes: RedirectAttributes): String {
        if (cart.isEmpty) {
            return fillMessageAndRedirectToIndex(redirectAttributes)
        }
        orderService.executeOrder(user.username, cart)
        cart.clear()
        redirectAttributes.addFlashAttribute("message", "Thank you!")
        return "redirect:/"
    }

    private fun fillMessageAndRedirectToIndex(redirectAttributes: RedirectAttributes): String {
        redirectAttributes.addFlashAttribute("message", "Your Cart is Empty.")
        return "redirect:/"
    }
}
