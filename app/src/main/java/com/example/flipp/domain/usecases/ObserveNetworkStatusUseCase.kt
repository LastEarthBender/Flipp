package com.example.flipp.domain.usecases

import com.example.flipp.domain.repositories.NetworkStatusRepository
import kotlinx.coroutines.flow.Flow

class ObserveNetworkStatusUseCase(private val networkStatusRepository: NetworkStatusRepository) {
    operator fun invoke(): Flow<Boolean> = networkStatusRepository.observeNetworkStatus()
}