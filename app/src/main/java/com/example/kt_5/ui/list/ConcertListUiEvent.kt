package com.example.kt_5.ui.list


sealed interface ConcertListUiEvent {
    object LoadConcerts : ConcertListUiEvent
    object RetryLoad : ConcertListUiEvent
    data class CityFilterChanged(val city: String) : ConcertListUiEvent
    data class OnConcertClick(val concertId: Int) : ConcertListUiEvent
}