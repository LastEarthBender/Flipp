package com.example.flipp.domain.usecases

import com.example.flipp.data.db.toDomainModel
import com.example.flipp.domain.models.Item
import com.example.flipp.domain.repositories.ItemRepository

class GetItemByIdUseCase(private val itemRepository: ItemRepository) {
    suspend operator fun invoke(id: Long): Item {
        val itemFromRoom = itemRepository.getItemFromRoom(id)

        return itemFromRoom?.toDomainModel() ?: itemRepository.getItemById(id)
    }
}
