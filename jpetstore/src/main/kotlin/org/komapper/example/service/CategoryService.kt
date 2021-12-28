package org.komapper.example.service

import org.springframework.stereotype.Service
import org.komapper.example.entity.Category
import org.komapper.example.repository.CategoryRepository

@Service
class CategoryService(private val categoryRepository: CategoryRepository) {
    fun getCategory(categoryId: String): Category? {
        return categoryRepository.fetchCategory(categoryId)
    }
}
