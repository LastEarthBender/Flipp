package com.example.flipp.domain.usecases

import com.example.flipp.data.db.toDomainModel
import com.example.flipp.domain.models.Item
import com.example.flipp.domain.repositories.ItemRepository


class GetItemsUseCase(private val itemRepository: ItemRepository) {
    suspend operator fun invoke(): List<Item> {
        val itemsFromRoom = itemRepository.getItemsFromRoom()

        return if (itemsFromRoom.isNotEmpty()) {
            itemsFromRoom.map { it.toDomainModel() }
        } else {
            itemRepository.getItems()
        }
    }
}
