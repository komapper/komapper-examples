package org.komapper.example.web.controller

import org.komapper.example.service.ItemService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.server.ResponseStatusException

@Controller
@RequestMapping("/item")
@Transactional
class ItemController(private val itemService: ItemService) {
    @GetMapping("/{itemId}")
    fun viewDetail(@PathVariable itemId: String, model: Model): String {
        val itemAggregate = itemService.getItemAggregate(itemId)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "itemId=$itemId not found")
        model.addAttribute("item", itemAggregate.item)
        model.addAttribute("inventory", itemAggregate.inventory)
        model.addAttribute("product", itemAggregate.product)
        return "item/detail"
    }
}
