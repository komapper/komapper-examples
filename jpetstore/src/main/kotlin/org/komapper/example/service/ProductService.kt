package org.komapper.example.service

import org.komapper.example.entity.Product
import org.komapper.example.model.ProductAggregate
import org.komapper.example.repository.ProductRepository
import org.springframework.stereotype.Service
import java.util.StringTokenizer

@Service
class ProductService(private val productRepository: ProductRepository) {

    fun getProductListByCategory(categoryId: String): List<Product> {
        return productRepository.fetchProductListByCategoryId(categoryId)
    }

    fun getProductListByKeywords(keywords: String): List<Product> {
        val keywordList: MutableList<String> = ArrayList()
        val tokenizer = StringTokenizer(keywords.lowercase(), " ", false)
        while (tokenizer.hasMoreTokens()) {
            keywordList.add(tokenizer.nextToken())
        }
        return productRepository.fetchProductListByKeywords(keywordList)
    }

    fun getProductAggregate(productId: String): ProductAggregate? {
        return productRepository.fetchProductAggregate(productId)
    }
}
