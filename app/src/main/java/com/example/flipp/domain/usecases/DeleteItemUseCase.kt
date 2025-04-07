package com.example.flipp.domain.usecases

import com.example.flipp.domain.repositories.ItemRepository


class DeleteItemUseCase(private val itemRepository: ItemRepository, ) {
    suspend operator fun invoke(id: Long): Boolean {
        val apiDeletionSuccess = itemRepository.deleteItem(id)

        val roomDeletionSuccess = itemRepository.deleteItemFromRoom(id)

        return apiDeletionSuccess && roomDeletionSuccess
    }
}
