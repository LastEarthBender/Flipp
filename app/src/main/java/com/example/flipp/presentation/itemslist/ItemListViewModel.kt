package com.example.flipp.presentation.itemslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flipp.domain.models.Item
import com.example.flipp.domain.usecases.GetItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemListViewModel @Inject constructor(
    private val getItemsUseCase: GetItemsUseCase,
) : ViewModel() {


    private val _itemsUiState = MutableLiveData<ItemsUiState>(ItemsUiState.Loading)
    val itemsUiState: LiveData<ItemsUiState> = _itemsUiState

    init {
        loadItems()
    }

    fun loadItems() {
        viewModelScope.launch {
            _itemsUiState.value = ItemsUiState.Loading
            try {
                val items = getItemsUseCase()
                _itemsUiState.value = ItemsUiState.Success(items)
            } catch (e: Exception) {
                _itemsUiState.value = ItemsUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    sealed class ItemsUiState {
        object Loading : ItemsUiState()
        data class Success(val items: List<Item>) : ItemsUiState()
        data class Error(val message: String) : ItemsUiState()
    }
}