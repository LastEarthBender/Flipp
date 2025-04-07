package com.example.flipp.presentation.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flipp.domain.models.*
import com.example.flipp.domain.repositories.ItemRepository
import com.example.flipp.domain.usecases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
    private val getDashboardSummaryUseCase: GetDashboardSummaryUseCase,
    private val getCategoryBreakdownUseCase: GetCategoryBreakdownUseCase,
) : ViewModel() {

    private val _dashboardUiState = MutableLiveData<DashboardUiState>(DashboardUiState.Loading)
    val dashboardUiState: LiveData<DashboardUiState> = _dashboardUiState

    init {
        viewModelScope.launch {
            if (itemRepository.getItemsFromRoom().isEmpty()) {
                itemRepository.populateDatabaseWithMockData()
            }
            loadDashboardData()
        }
    }


    fun loadDashboardData() {
        viewModelScope.launch {
            _dashboardUiState.value = DashboardUiState.Loading
            try {
                val summary = getDashboardSummaryUseCase()
                val categories = getCategoryBreakdownUseCase()
                _dashboardUiState.value = DashboardUiState.Success(summary, categories)
            } catch (e: Exception) {
                _dashboardUiState.value =
                    DashboardUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    sealed class DashboardUiState {
        object Loading : DashboardUiState()
        data class Success(
            val summary: DashboardSummary,
            val categories: List<Category>,
        ) : DashboardUiState()

        data class Error(val message: String) : DashboardUiState()
    }
}
