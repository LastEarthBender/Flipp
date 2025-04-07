package com.example.flipp.domain.usecases

import com.example.flipp.domain.models.Item
import com.example.flipp.domain.repositories.ItemRepository

class AddItemUseCase(private val itemRepository: ItemRepository) {
    suspend operator fun invoke(item: Item): Long {
        val apiAdditionSuccess = itemRepository.addItem(item)

        val roomAdditionSuccess = itemRepository.addItemToRoom(item)

        return if (apiAdditionSuccess > 0 && roomAdditionSuccess > 0) apiAdditionSuccess else -1
    }
}
