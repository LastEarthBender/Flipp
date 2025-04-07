package com.example.flipp.presentation.itemadd

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flipp.domain.models.Item
import com.example.flipp.domain.usecases.AddItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemAddViewModel @Inject constructor(
    private val addItemUseCase: AddItemUseCase
) : ViewModel() {

    private val _addUiState = MutableLiveData<AddUiState>(AddUiState.Loading)
    val addUiState: LiveData<AddUiState> = _addUiState

    fun addItem(item: Item) {
        viewModelScope.launch {
            _addUiState.value = AddUiState.Loading
            try {
                val itemId = addItemUseCase(item)
                if (itemId > 0) {
                    _addUiState.value = AddUiState.Success
                } else {
                    _addUiState.value = AddUiState.Error("Failed to add item")
                }
            } catch (e: Exception) {
                _addUiState.value = AddUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    sealed class AddUiState {
        object Loading : AddUiState()
        object Success : AddUiState()
        data class Error(val message: String) : AddUiState()
    }
}


