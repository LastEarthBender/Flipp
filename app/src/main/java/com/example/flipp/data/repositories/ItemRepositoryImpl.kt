package com.example.flipp.data.repositories

import com.example.flipp.data.api.ApiService
import com.example.flipp.data.api.MockApiService
import com.example.flipp.data.db.ItemDao
import com.example.flipp.data.db.ItemEntity
import com.example.flipp.data.db.toDomainModel
import com.example.flipp.data.db.toEntity
import com.example.flipp.data.models.Response
import com.example.flipp.data.models.toDomainModel
import com.example.flipp.data.models.toDto
import com.example.flipp.data.models.toEntity
import com.example.flipp.domain.models.Category
import com.example.flipp.domain.models.DashboardSummary
import com.example.flipp.domain.models.Item
import com.example.flipp.domain.repositories.ItemRepository
import kotlinx.coroutines.flow.first

class ItemRepositoryImpl(private val itemDao: ItemDao, private val apiService: ApiService, ) : ItemRepository {

    override suspend fun populateDatabaseWithMockData() {
        val mockItems = (apiService as? MockApiService)?.getItems()?.let { response ->
            when (response) {
                is Response.Success -> response.data
                is Response.Error -> throw Exception("Failed to fetch mock items: ${response.message}")
                else -> throw Exception("Unexpected response type")
            }
        } ?: throw Exception("Failed to fetch mock items")

        mockItems.forEach { item ->
            itemDao.insertItem(item.toEntity())
        }
    }

    override suspend fun getItems(): List<Item> {
        return (apiService as? MockApiService)?.getItems()?.let { response ->
            when (response) {
                is Response.Success -> response.data.map { it.toDomainModel() }
                is Response.Error -> throw Exception("Failed to fetch items: ${response.message}")
                else -> throw Exception("Unexpected response type")
            }
        } ?: throw Exception("Failed to fetch items")
    }
    override suspend fun getItemsFromRoom(): List<ItemEntity> {
        return itemDao.getAllItems().first()
    }

    override suspend fun getItemById(id: Long): Item {
        return (apiService as? MockApiService)?.getItemById(id)?.let { response ->
            when (response) {
                is Response.Success -> response.data.toDomainModel()
                is Response.Error -> throw NoSuchElementException("Item not found: ${response.message}")
                else -> throw Exception("Unexpected response type")
            }
        } ?: throw NoSuchElementException("Item not found")
    }


    override suspend fun getItemFromRoom(id: Long): ItemEntity? {
        return itemDao.getItemById(id)
    }

    override suspend fun addItem(item: Item): Long {
        val response = (apiService as? MockApiService)?.createItem(item.toDto())
        return when (response) {
            is Response.Success -> response.data.id!!
            is Response.Error -> throw Exception("Failed to add item: ${response.message}")
            else -> throw Exception("Unexpected response type")
        }
    }

    override suspend fun addItemToRoom(item: Item): Long {
        return itemDao.insertItem(item.toEntity())
    }

    override suspend fun updateItem(item: Item): Boolean {
        val response = (apiService as? MockApiService)?.updateItem(item.id, item.toDto())
        return when (response) {
            is Response.Success -> true
            is Response.Error -> false
            else -> throw Exception("Unexpected response type")
        }
    }

    override suspend fun updateItemInRoom(item: Item): Boolean {
        return itemDao.updateItem(item.toEntity()) > 0
    }

    override suspend fun deleteItem(id: Long): Boolean {
        val response = (apiService as? MockApiService)?.deleteItem(id)
        return when (response) {
            is Response.Success -> true
            is Response.Error -> false
            else -> throw Exception("Unexpected response type")
        }
    }

    override suspend fun deleteItemFromRoom(id: Long): Boolean {
        val itemEntity = itemDao.getItemById(id)
        return if (itemEntity != null) {
            val rowsAffected = itemDao.deleteItem(itemEntity)
            rowsAffected > 0
        } else {
            false
        }
    }

    override suspend fun getDashboardSummary(): DashboardSummary {
        val items = getItemsFromRoom().map { it.toDomainModel() }
        val totalItems = items.size
        val outOfStockItems = items.count { it.quantity == 0 }
        val lowStockItems = items.count { it.quantity > 0 && it.quantity < 10 }
        val recentlyAddedItems = items.count { it.lastUpdated > System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000) }
        val categories = items.groupBy { it.category }.map {
            Category(name = it.key, itemCount = it.value.size)
        }

        return DashboardSummary(
            totalItems = totalItems,
            outOfStockItems = outOfStockItems,
            lowStockItems = lowStockItems,
            recentlyAddedItems = recentlyAddedItems,
            categories = categories
        )
    }

    override suspend fun getCategoryBreakdown(): List<Category> {
        val items = getItemsFromRoom().map { it.toDomainModel() }
        return items.groupBy { it.category }.map {
            Category(name = it.key, itemCount = it.value.size)
        }
    }

}
