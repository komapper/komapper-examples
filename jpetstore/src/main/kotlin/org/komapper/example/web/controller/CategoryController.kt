package org.komapper.example.web.controller

import org.komapper.example.service.CategoryService
import org.komapper.example.service.ProductService
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/category")
@Transactional
class CategoryController(private val categoryService: CategoryService, private val productService: ProductService) {
    @GetMapping("/{id}")
    fun category(@PathVariable id: String?, model: Model): String {
        model.addAttribute("category", categoryService.getCategory(id!!))
        model.addAttribute("productList", productService.getProductListByCategory(id))
        return "category/list"
    }
}
