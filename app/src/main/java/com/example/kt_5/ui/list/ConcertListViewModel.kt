package com.example.kt_5.ui.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kt_5.data.repository.ConcertRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ConcertListViewModel(
    private val repository: ConcertRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ConcertListUiState>(ConcertListUiState.Loading)
    val uiState: StateFlow<ConcertListUiState> = _uiState.asStateFlow()

    private var isDataLoaded = false

    fun onEvent(event: ConcertListUiEvent) {
        when (event) {
            ConcertListUiEvent.LoadConcerts -> loadConcerts()
            ConcertListUiEvent.RetryLoad -> {
                isDataLoaded = false
                loadConcerts()
            }
            is ConcertListUiEvent.OnConcertClick -> { }
        }
    }

    private fun loadConcerts() {

        viewModelScope.launch {
            if (!isDataLoaded) {
                _uiState.value = ConcertListUiState.Loading
                repository.refreshConcerts()
                    .onSuccess { isDataLoaded = true }
                    .onFailure { error ->
                        Log.e("CONCERT_APP", "Ошибка: ${error.message}")
                        _uiState.value = ConcertListUiState.Error(
                            error.message ?: "Ошибка загрузки"
                        )
                        return@launch
                    }
            }

            repository.getAllConcerts().collect { concerts ->
                if (concerts.isEmpty()) {
                    _uiState.value = ConcertListUiState.Empty
                } else {
                    _uiState.value = ConcertListUiState.Success(concerts)
                }
            }
        }
    }
}