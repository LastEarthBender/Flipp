package com.example.flipp.domain.models

data class Item(
    val id: Long = 0,
    val name: String,
    val description: String,
    val quantity: Int,
    val price: Double,
    val category: String,
    val imageUrl: String?,
    val supplierName: String,
    val supplierContact: String,
    val lastUpdated: Long = System.currentTimeMillis(),
)