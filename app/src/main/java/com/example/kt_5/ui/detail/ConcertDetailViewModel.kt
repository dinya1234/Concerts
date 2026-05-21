package com.example.kt_5.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kt_5.data.repository.ConcertRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ConcertDetailViewModel(
    private val repository: ConcertRepository,
    private val concertId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow<ConcertDetailUiState>(ConcertDetailUiState.Loading)
    val uiState: StateFlow<ConcertDetailUiState> = _uiState.asStateFlow()

    init {
        loadConcert()
    }

    fun onEvent(event: ConcertDetailUiEvent) {
        when (event) {
            ConcertDetailUiEvent.LoadConcert -> loadConcert()
        }
    }

    private fun loadConcert() {
        _uiState.value = ConcertDetailUiState.Loading

        viewModelScope.launch {
            val concert = repository.getConcertById(concertId)
            Log.d("CONCERT_APP", "getConcertById($concertId) = $concert")
            if (concert != null) {
                _uiState.value = ConcertDetailUiState.Success(concert)
            } else {
                _uiState.value = ConcertDetailUiState.Error("Концерт не найден")
            }
        }
    }
}