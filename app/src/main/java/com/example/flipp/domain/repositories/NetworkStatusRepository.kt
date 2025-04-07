package com.example.flipp.domain.repositories

import kotlinx.coroutines.flow.Flow

interface NetworkStatusRepository {
    fun isOnline(): Boolean
    fun observeNetworkStatus(): Flow<Boolean>
}