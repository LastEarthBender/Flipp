package com.example.flipp.presentation.reports

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class ReportViewModel @Inject constructor(
) : ViewModel() {

    private val _reportUiState = MutableLiveData<ReportUiState>(ReportUiState.Loading)
    val reportUiState: LiveData<ReportUiState> = _reportUiState

    fun loadReportData() {
        viewModelScope.launch {
            _reportUiState.value = ReportUiState.Loading
            try {
                val quantityTrends = generateQuantityTrends()

                val topCategories = generateTopCategories()

                val lowStockItems = generateLowStockItems()

                _reportUiState.value = ReportUiState.Success(quantityTrends, topCategories, lowStockItems)
            } catch (e: Exception) {
                _reportUiState.value = ReportUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    private fun generateQuantityTrends(): List<Pair<Date, Int>> {
        val calendar = Calendar.getInstance()
        val quantityTrends = mutableListOf<Pair<Date, Int>>()

        for (i in 0..6) {
            calendar.add(Calendar.DAY_OF_MONTH, -1)
            val date = calendar.time
            val quantity = (10..50).random()
            quantityTrends.add(date to quantity)
        }

        return quantityTrends.reversed()
    }

    private fun generateTopCategories(): List<Pair<String, Int>> {
        return listOf(
            "Electronics" to (10..30).random(),
            "Clothing" to (10..30).random(),
            "Furniture" to (10..30).random(),
            "Books" to (10..30).random(),
            "Food" to (10..30).random()
        )
    }

    private fun generateLowStockItems(): List<String> {

        return listOf(
            "Furniture - Only 5 left!",
            "Clothing - Only 2 left!",
            "Books - Only 10 left!"
        )
    }

    sealed class ReportUiState {
        object Loading : ReportUiState()
        data class Success(
            val quantityTrends: List<Pair<Date, Int>>,
            val topCategories: List<Pair<String, Int>>,
            val lowStockItems: List<String>
        ) : ReportUiState()

        data class Error(val message: String) : ReportUiState()
    }
}
