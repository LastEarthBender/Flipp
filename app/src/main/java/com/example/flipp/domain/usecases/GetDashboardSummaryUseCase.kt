package com.example.flipp.domain.usecases

import com.example.flipp.domain.models.DashboardSummary
import com.example.flipp.domain.repositories.ItemRepository

class GetDashboardSummaryUseCase(private val itemRepository: ItemRepository) {
    suspend operator fun invoke(): DashboardSummary = itemRepository.getDashboardSummary()
}