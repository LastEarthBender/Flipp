package com.example.flipp.presentation.itemedit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flipp.domain.models.Item
import com.example.flipp.domain.usecases.*
import com.example.flipp.presentation.itemdetail.ItemDetailViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemEditViewModel @Inject constructor(
    private val updateItemUseCase: UpdateItemUseCase,
    private val getItemByIdUseCase: GetItemByIdUseCase,

    ) : ViewModel() {

    private val _editUiState = MutableLiveData<EditUiState>(EditUiState.Loading)
    val editUiState: LiveData<EditUiState> = _editUiState

    private val _itemUiState = MutableLiveData<ItemDetailViewModel.ItemUiState>()
    val itemUiState: LiveData<ItemDetailViewModel.ItemUiState> = _itemUiState

    fun loadItem(id: Long) {
        viewModelScope.launch {
            _itemUiState.value = ItemDetailViewModel.ItemUiState.Loading
            try {
                val item = getItemByIdUseCase(id)
                _itemUiState.value = ItemDetailViewModel.ItemUiState.Success(item)
            } catch (e: Exception) {
                _itemUiState.value =
                    ItemDetailViewModel.ItemUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun updateItem(item: Item) {
        viewModelScope.launch {
            _editUiState.value = EditUiState.Loading
            try {
                val success = updateItemUseCase(item)
                if (success) {
                    _editUiState.value = EditUiState.Success
                } else {
                    _editUiState.value = EditUiState.Error("Failed to update item")
                }
            } catch (e: Exception) {
                _editUiState.value = EditUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    sealed class ItemUiState {
        object Loading : ItemUiState()
        data class Success(val item: Item) : ItemUiState()
        data class Error(val message: String) : ItemUiState()
    }

    sealed class EditUiState {
        object Loading : EditUiState()
        object Success : EditUiState()
        data class Error(val message: String) : EditUiState()
    }
}
