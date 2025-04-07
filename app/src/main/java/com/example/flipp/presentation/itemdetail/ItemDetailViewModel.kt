package com.example.flipp.presentation.itemdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flipp.domain.models.Item
import com.example.flipp.domain.usecases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemDetailViewModel @Inject constructor(
    private val getItemByIdUseCase: GetItemByIdUseCase,
    private val deleteItemUseCase: DeleteItemUseCase,
) : ViewModel() {

    private val _itemUiState = MutableLiveData<ItemUiState>()
    val itemUiState: LiveData<ItemUiState> = _itemUiState

    private val _deleteState = MutableLiveData<DeleteUiState>()
    val deleteState: LiveData<DeleteUiState> = _deleteState


    fun loadItem(id: Long) {
        viewModelScope.launch {
            _itemUiState.value = ItemUiState.Loading
            try {
                val item = getItemByIdUseCase(id)
                _itemUiState.value = ItemUiState.Success(item)
            } catch (e: Exception) {
                _itemUiState.value = ItemUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun deleteItem(id: Long) {
        viewModelScope.launch {
            _deleteState.value = DeleteUiState.Loading
            try {
                val success = deleteItemUseCase(id)
                if (success) {
                    _deleteState.value = DeleteUiState.Success
                } else {
                    _deleteState.value = DeleteUiState.Error("Failed to delete item")
                }
            } catch (e: Exception) {
                _deleteState.value = DeleteUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    sealed class ItemUiState {
        object Loading : ItemUiState()
        data class Success(val item: Item) : ItemUiState()
        data class Error(val message: String) : ItemUiState()
    }

    sealed class DeleteUiState {
        object Loading : DeleteUiState()
        object Success : DeleteUiState()
        data class Error(val message: String) : DeleteUiState()
    }
}