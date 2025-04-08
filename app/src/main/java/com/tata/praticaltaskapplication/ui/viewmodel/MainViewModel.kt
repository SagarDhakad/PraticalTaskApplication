package com.tata.praticaltaskapplication.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tata.praticaltaskapplication.model.DataModel
import com.tata.praticaltaskapplication.repo.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _dataModelLiveData = MutableStateFlow<List<DataModel>>(emptyList())
    val dataModelLiveData: StateFlow<List<DataModel>> = _dataModelLiveData

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun getData(maxLength: Int, inputText: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = mainRepository.getData(maxLength, inputText)
            result.onSuccess { model ->
                _dataModelLiveData.value = listOf(model) + _dataModelLiveData.value
                _error.value = null
            }.onFailure {
                _error.value = it.message
            }
            _isLoading.value = false
        }
    }

    fun deleteAllData() {
        _dataModelLiveData.value = emptyList()
    }

    fun deleteOneData(item: DataModel) {
        _dataModelLiveData.value = _dataModelLiveData.value.toMutableList().apply { remove(item) }
    }
}