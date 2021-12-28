package org.komapper.example.service

import org.komapper.example.model.ItemAggregate
import org.komapper.example.repository.ItemRepository
import org.springframework.stereotype.Service

@Service
class ItemService(private val itemRepository: ItemRepository) {
    fun getItemAggregate(itemId: String): ItemAggregate? {
        return itemRepository.fetchItemAggregate(itemId)
    }
}
