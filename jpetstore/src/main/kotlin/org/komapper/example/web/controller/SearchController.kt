package org.komapper.example.web.controller

import org.komapper.example.service.ProductService
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/search")
@Transactional
class SearchController(private val productService: ProductService) {
    @GetMapping
    fun search(
        @RequestParam(required = false, defaultValue = "") keyword: String?,
        model: Model,
    ): String {
        val productList = productService.getProductListByKeywords(keyword!!)
        model.addAttribute("productList", productList)
        return "search/list"
    }
}
