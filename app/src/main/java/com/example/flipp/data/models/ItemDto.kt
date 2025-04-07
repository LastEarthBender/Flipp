package com.example.flipp.data.models

import com.example.flipp.data.db.ItemEntity
import com.example.flipp.domain.models.Item
import com.google.gson.annotations.SerializedName

data class ItemDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("price") val price: Double,
    @SerializedName("category") val category: String,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("supplier_name") val supplierName: String,
    @SerializedName("supplier_contact") val supplierContact: String,
    @SerializedName("last_updated") val lastUpdated: Long
)

fun ItemDto.toDomainModel(): Item {
    return Item(
        id = id ?: 0,
        name = name,
        description = description ,
        quantity = quantity,
        price = price,
        category = category,
        imageUrl = imageUrl,
        supplierName = supplierName,
        supplierContact = supplierContact,
        lastUpdated = lastUpdated
    )
}

fun Item.toDto(): ItemDto {
    return ItemDto(
        id = if (id == 0L) null else id,
        name = name,
        description = description,
        quantity = quantity,
        price = price,
        category = category,
        imageUrl = imageUrl,
        supplierName = supplierName ,
        supplierContact = supplierContact,
        lastUpdated = lastUpdated
    )
}

fun ItemDto.toEntity(): ItemEntity {
    return ItemEntity(
        id = id ?: 0,
        name = name,
        description = description,
        quantity = quantity,
        price = price,
        category = category,
        imageUrl = imageUrl,
        supplierName = supplierName,
        supplierContact = supplierContact,
        lastUpdated = lastUpdated
    )
}
