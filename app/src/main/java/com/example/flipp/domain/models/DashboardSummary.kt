package com.example.flipp.domain.models

data class DashboardSummary(
    val totalItems: Int,
    val outOfStockItems: Int,
    val lowStockItems: Int,
    val recentlyAddedItems: Int,
    val categories: List<Category>,
)