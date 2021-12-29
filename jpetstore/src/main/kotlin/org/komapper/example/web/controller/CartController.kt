package org.komapper.example.web.controller

import org.komapper.example.model.Cart
import org.komapper.example.service.ItemService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.server.ResponseStatusException

@Controller
@RequestMapping("/cart")
@Transactional
class CartController(private val itemService: ItemService, private val cart: Cart) {
    @GetMapping
    fun view(model: Model): String {
        val cartForm = CartForm()
        for (cartItem in cart.cartItemList) {
            val cartItemForm = CartItemForm()
            cartItemForm.quantity = cartItem.quantity
            cartForm.items[cartItem.item.itemId] = cartItemForm
        }
        model.addAttribute("cart", cart)
        model.addAttribute("cartForm", cartForm)
        return "cart/list"
    }

    @PostMapping
    fun updateAll(@Validated cartForm: CartForm, result: BindingResult, model: Model): String {
        if (result.hasErrors()) {
            model.addAttribute("cart", cart)
            model.addAttribute("cartForm", cartForm)
            return "cart/list"
        }
        for ((itemId, cartItemForm) in cartForm.items) {
            if (cart.containsItemId(itemId)) {
                if (cartItemForm.quantity!! < 1) {
                    cart.removeItemById(itemId)
                } else {
                    cart.setQuantityByItemId(itemId, cartItemForm.quantity!!)
                }
            }
        }
        return "redirect:/cart"
    }

    @PostMapping("/item/{itemId}")
    fun addItem(@PathVariable itemId: String, model: Model): String {
        if (cart.containsItemId(itemId)) {
            cart.incrementQuantity(itemId)
        } else {
            val itemAggregate = itemService.getItemAggregate(itemId)
                ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "itemId=$itemId not found")
            cart.addItem(itemAggregate.item, itemAggregate.product, itemAggregate.inventory)
        }
        model.addAttribute(cart)
        return "redirect:/cart"
    }

    @DeleteMapping("/item/{itemId}")
    fun removeItem(@PathVariable itemId: String): String {
        cart.removeItemById(itemId)
        return "redirect:/cart"
    }

    @GetMapping("/checkout")
    fun checkout(model: Model): String {
        model.addAttribute("cart", cart)
        return "cart/checkout"
    }

    @PostMapping("/checkout")
    fun checkout(): String {
        return "redirect:/order"
    }
}
