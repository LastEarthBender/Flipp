package com.example.flipp.data.di

import android.content.Context
import androidx.room.Room
import com.example.flipp.data.api.ApiService
import com.example.flipp.data.db.AppDatabase
import com.example.flipp.data.db.ItemDao
import com.example.flipp.data.db.MIGRATION_1_2
import com.example.flipp.data.repositories.ItemRepositoryImpl
import com.example.flipp.data.repositories.NetworkStatusRepositoryImpl
import com.example.flipp.domain.repositories.ItemRepository
import com.example.flipp.domain.repositories.NetworkStatusRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "inventory_db"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Singleton
    @Provides
    fun provideItemDao(database: AppDatabase): ItemDao {
        return database.itemDao()
    }

    @Singleton
    @Provides
    fun provideItemRepository(itemDao: ItemDao, apiService: ApiService): ItemRepository {
        return ItemRepositoryImpl(itemDao, apiService)
    }

    @Singleton
    @Provides
    fun provideNetworkStatusRepository(@ApplicationContext context: Context): NetworkStatusRepository {
        return NetworkStatusRepositoryImpl(context)
    }
}
