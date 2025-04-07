package com.example.flipp.domain.usecases

import com.example.flipp.domain.models.Category
import com.example.flipp.domain.repositories.ItemRepository

class GetCategoryBreakdownUseCase(private val itemRepository: ItemRepository) {
    suspend operator fun invoke(): List<Category> = itemRepository.getCategoryBreakdown()
}