package com.example.kt_5.ui.detail

import com.example.kt_5.data.local.entity.ConcertEntity

sealed interface ConcertDetailUiState {
    object Loading : ConcertDetailUiState
    data class Success(val concert: ConcertEntity) : ConcertDetailUiState
    data class Error(val message: String) : ConcertDetailUiState
}