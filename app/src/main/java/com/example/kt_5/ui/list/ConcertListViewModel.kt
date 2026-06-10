package com.example.kt_5.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kt_5.data.repository.ConcertRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConcertListViewModel @Inject constructor(
    private val repository: ConcertRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ConcertListUiState>(ConcertListUiState.Loading)
    val uiState: StateFlow<ConcertListUiState> = _uiState.asStateFlow()

    private var isDataLoaded = false
    private var cityFilter = ""
    private var observeJob: Job? = null
    private var offlineMessage: String? = null

    fun onEvent(event: ConcertListUiEvent) {
        when (event) {
            ConcertListUiEvent.LoadConcerts -> loadConcerts()
            ConcertListUiEvent.RetryLoad -> {
                isDataLoaded = false
                offlineMessage = null
                loadConcerts()
            }
            is ConcertListUiEvent.CityFilterChanged -> {
                cityFilter = event.city
                observeConcerts()
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
                        if (!repository.hasCachedConcerts()) {
                            _uiState.value = ConcertListUiState.Error(
                                error.message ?: "Ошибка загрузки"
                            )
                            return@launch
                        }
                        offlineMessage = "Нет сети. Показаны сохранённые концерты"
                    }
            }
            observeConcerts()
        }
    }

    private fun observeConcerts() {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            val concertsFlow = if (cityFilter.isBlank()) {
                repository.getAllConcerts()
            } else {
                repository.getConcertsByCity(cityFilter)
            }

            concertsFlow.collect { concerts ->
                if (concerts.isEmpty()) {
                    _uiState.value = if (cityFilter.isBlank()) {
                        ConcertListUiState.Empty
                    } else {
                        ConcertListUiState.Error("Нет концертов в городе «$cityFilter»")
                    }
                } else {
                    _uiState.value = ConcertListUiState.Success(concerts, offlineMessage)
                }
            }
        }
    }
}
