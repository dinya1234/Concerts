package com.example.kt_5.ui.detail

sealed interface ConcertDetailUiEvent {
    object LoadConcert : ConcertDetailUiEvent
    object ToggleFavorite : ConcertDetailUiEvent
}