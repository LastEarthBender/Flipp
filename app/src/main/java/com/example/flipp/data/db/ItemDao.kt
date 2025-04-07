package com.example.flipp.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM items ORDER BY last_updated DESC")
    fun getAllItems(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun getItemById(id: Long): ItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ItemEntity): Long

    @Update
    suspend fun updateItem(item: ItemEntity): Int

    @Delete
    suspend fun deleteItem(item: ItemEntity): Int

    @Query("SELECT COUNT(*) FROM items")
    suspend fun getTotalItemCount(): Int

    @Query("SELECT COUNT(*) FROM items WHERE quantity = 0")
    suspend fun getOutOfStockItemCount(): Int

    @Query("SELECT COUNT(*) FROM items WHERE quantity < 10 AND quantity > 0")
    suspend fun getLowStockItemCount(): Int

    @Query("SELECT COUNT(*) FROM items WHERE last_updated > :timestamp")
    suspend fun getRecentItemCount(timestamp: Long): Int

    @Query("SELECT category, COUNT(*) as count FROM items GROUP BY category")
    suspend fun getCategoryBreakdown(): List<CategoryCount>
}