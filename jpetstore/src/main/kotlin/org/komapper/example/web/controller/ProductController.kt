package org.komapper.example.web.controller

import org.komapper.example.service.ProductService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.server.ResponseStatusException

@Controller
@RequestMapping("/product")
@Transactional
class ProductController(private val productService: ProductService) {
    @GetMapping("/{productId}")
    fun product(@PathVariable productId: String, model: Model): String {
        val productAggregate = productService.getProductAggregate(productId)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "productId=$productId not found")
        model.addAttribute("product", productAggregate.product)
        model.addAttribute("itemList", productAggregate.itemSet)
        return "product/list"
    }
}
