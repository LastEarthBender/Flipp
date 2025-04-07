package com.example.flipp.domain.usecases

import com.example.flipp.domain.models.Item
import com.example.flipp.domain.repositories.ItemRepository

class UpdateItemUseCase(private val itemRepository: ItemRepository) {
    suspend operator fun invoke(item: Item): Boolean {
        val apiUpdateSuccess = itemRepository.updateItem(item)

        val roomUpdateSuccess = itemRepository.updateItemInRoom(item)

        return apiUpdateSuccess && roomUpdateSuccess
    }
}
