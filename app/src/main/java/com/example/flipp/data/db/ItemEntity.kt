package com.example.flipp.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.flipp.domain.models.Item

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String,
    val quantity: Int,
    val price: Double,
    val category: String,
    val imageUrl: String?,
    val supplierName: String,
    val supplierContact: String,
    @ColumnInfo(name = "last_updated") val lastUpdated: Long
)

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE items ADD COLUMN last_updated INTEGER NOT NULL DEFAULT 0")
    }
}



data class CategoryCount(
    val category: String,
    val count: Int
)

fun ItemEntity.toDomainModel(): Item {
    return Item(
        id = id,
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

fun Item.toEntity(): ItemEntity {
    return ItemEntity(
        id = id,
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