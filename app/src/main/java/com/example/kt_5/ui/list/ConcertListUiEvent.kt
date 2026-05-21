package com.example.kt_5.ui.list


sealed interface ConcertListUiEvent {
    object LoadConcerts : ConcertListUiEvent
    object RetryLoad : ConcertListUiEvent
    data class OnConcertClick(val concertId: Int) : ConcertListUiEvent
}