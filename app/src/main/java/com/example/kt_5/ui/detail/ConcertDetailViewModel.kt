package com.example.kt_5.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kt_5.data.repository.ConcertRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConcertDetailViewModel @Inject constructor(
    private val repository: ConcertRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ConcertDetailUiState>(ConcertDetailUiState.Loading)
    val uiState: StateFlow<ConcertDetailUiState> = _uiState.asStateFlow()

    private var concertId: Int = 0

    fun initialize(concertId: Int) {
        this.concertId = concertId
        loadConcert()
    }

    fun onEvent(event: ConcertDetailUiEvent) {
        when (event) {
            ConcertDetailUiEvent.LoadConcert -> loadConcert()
            ConcertDetailUiEvent.ToggleFavorite -> toggleFavorite()
        }
    }

    private fun loadConcert() {
        _uiState.value = ConcertDetailUiState.Loading
        viewModelScope.launch {
            val concert = repository.getConcertById(concertId)
            if (concert != null) {
                _uiState.value = ConcertDetailUiState.Success(concert)
            } else {
                _uiState.value = ConcertDetailUiState.Error("Концерт не найден")
            }
        }
    }

    private fun toggleFavorite() {
        val concert = (_uiState.value as? ConcertDetailUiState.Success)?.concert ?: return
        viewModelScope.launch {
            val newValue = !concert.isFavorite
            repository.toggleFavorite(concert.id, newValue)
            _uiState.value = ConcertDetailUiState.Success(concert.copy(isFavorite = newValue))
        }
    }
}
