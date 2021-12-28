package org.komapper.example.service

import org.springframework.stereotype.Service
import org.komapper.example.model.ItemAggregate
import org.komapper.example.repository.ItemRepository

@Service
class ItemService(private val itemRepository: ItemRepository) {
    fun getItemAggregate(itemId: String): ItemAggregate? {
        return itemRepository.fetchItemAggregate(itemId)
    }
}
