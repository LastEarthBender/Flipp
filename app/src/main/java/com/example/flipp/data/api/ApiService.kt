package com.example.flipp.data.api

import com.example.flipp.data.models.ItemDto
import com.example.flipp.data.models.Response


class MockApiService : ApiService {
    private val items = mutableListOf<ItemDto>()
    private var nextId = 1L

    init {
        addSampleItems()
    }

    private fun addSampleItems() {
        val categories = listOf("Electronics", "Furniture", "Clothing", "Books", "Food")
        val suppliers = listOf("Chinaza", "Blossom", "Akpi", "Apple")

        for (i in 1..56) {
            val item = ItemDto(
                id = nextId++,
                name = "Product $i",
                description = "Description for item $i\nLorem ipsum dolor sit amet, consectetur adipiscing elit.",
                quantity = (1..100).random(),
                price = (10..1000).random() / 10.0,
                category = categories.random(),
                imageUrl = "https://picsum.photos/id/${i + 10}/200/200",
                supplierName = suppliers.random(),
                supplierContact = "contact@${suppliers.random().lowercase().replace(" ", "")}.com",
                lastUpdated = System.currentTimeMillis() - (0..7).random() * 86400000
            )
            items.add(item)
        }
    }

    override suspend fun getItems(): Response<List<ItemDto>> {
        return Response.Success(items)
    }

    override suspend fun getItemById(id: Long): Response<ItemDto> {
        val item = items.find { it.id == id }
        return if (item != null) {
            Response.Success(item)
        } else {
            Response.Error(404, "Item not found")
        }
    }

    override suspend fun createItem(item: ItemDto): Response<ItemDto> {
        val newItem = item.copy(id = nextId++)
        items.add(newItem)
        return Response.Success(newItem)
    }

    override suspend fun updateItem(id: Long, item: ItemDto): Response<ItemDto> {
        val index = items.indexOfFirst { it.id == id }
        return if (index != -1) {
            items[index] = item.copy(id = id)
            Response.Success(items[index])
        } else {
            Response.Error(404, "Item not found")
        }
    }

    override suspend fun deleteItem(id: Long): Response<Unit> {
        val removed = items.removeIf { it.id == id }
        return if (removed) {
            Response.Success(Unit)
        } else {
            Response.Error(404, "Item not found")
        }
    }
}

interface ApiService {
    suspend fun getItems(): Response<List<ItemDto>>
    suspend fun getItemById(id: Long): Response<ItemDto>
    suspend fun createItem(item: ItemDto): Response<ItemDto>
    suspend fun updateItem(id: Long, item: ItemDto): Response<ItemDto>
    suspend fun deleteItem(id: Long): Response<Unit>
}