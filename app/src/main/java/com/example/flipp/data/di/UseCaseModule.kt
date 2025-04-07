package com.example.flipp.data.di

import com.example.flipp.domain.repositories.ItemRepository
import com.example.flipp.domain.repositories.NetworkStatusRepository
import com.example.flipp.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Singleton
    @Provides
    fun provideAddItemUseCase(itemRepository: ItemRepository): AddItemUseCase {
        return AddItemUseCase(itemRepository)
    }

    @Singleton
    @Provides
    fun provideDeleteItemUseCase(itemRepository: ItemRepository): DeleteItemUseCase {
        return DeleteItemUseCase(itemRepository)
    }

    @Singleton
    @Provides
    fun provideGetCategoryBreakdownUseCase(itemRepository: ItemRepository): GetCategoryBreakdownUseCase {
        return GetCategoryBreakdownUseCase(itemRepository)
    }

    @Singleton
    @Provides
    fun provideGetDashboardSummaryUseCase(itemRepository: ItemRepository): GetDashboardSummaryUseCase {
        return GetDashboardSummaryUseCase(itemRepository)
    }

    @Singleton
    @Provides
    fun provideGetItemByIdUseCase(itemRepository: ItemRepository): GetItemByIdUseCase {
        return GetItemByIdUseCase(itemRepository)
    }

    @Singleton
    @Provides
    fun provideGetItemsUseCase(itemRepository: ItemRepository): GetItemsUseCase {
        return GetItemsUseCase(itemRepository)
    }

    @Singleton
    @Provides
    fun provideObserveNetworkStatusUseCase(networkStatusRepository: NetworkStatusRepository): ObserveNetworkStatusUseCase {
        return ObserveNetworkStatusUseCase(networkStatusRepository)
    }

    @Singleton
    @Provides
    fun provideUpdateItemUseCase(itemRepository: ItemRepository): UpdateItemUseCase {
        return UpdateItemUseCase(itemRepository)
    }
}
