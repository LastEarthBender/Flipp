package com.example.flipp.domain.repositories

import com.example.flipp.data.db.ItemEntity
import com.example.flipp.domain.models.*

interface ItemRepository {
    suspend fun getItems(): List<Item>
    suspend fun getItemById(id: Long): Item
    suspend fun addItem(item: Item): Long
    suspend fun updateItem(item: Item): Boolean
    suspend fun deleteItem(id: Long): Boolean

    suspend fun getItemsFromRoom(): List<ItemEntity>
    suspend fun getItemFromRoom(id: Long): ItemEntity?
    suspend fun addItemToRoom(item: Item): Long
    suspend fun updateItemInRoom(item: Item): Boolean
    suspend fun deleteItemFromRoom(id: Long): Boolean

    suspend fun getDashboardSummary(): DashboardSummary
    suspend fun getCategoryBreakdown(): List<Category>
    suspend fun populateDatabaseWithMockData()
}