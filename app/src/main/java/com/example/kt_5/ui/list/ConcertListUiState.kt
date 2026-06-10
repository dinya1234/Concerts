package com.example.kt_5.ui.list

import com.example.kt_5.data.local.entity.ConcertEntity

sealed interface ConcertListUiState {
    object Loading : ConcertListUiState
    data class Success(
        val concerts: List<ConcertEntity>,
        val offlineMessage: String? = null
    ) : ConcertListUiState
    data class Error(val message: String) : ConcertListUiState
    object Empty : ConcertListUiState
}