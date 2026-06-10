package com.example.kt_5.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ConcertDetailScreen(
    viewModel: ConcertDetailViewModel,
    concertId: Int,
    onBackClick: () -> Unit,
    onBuyClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(concertId) {
        viewModel.initialize(concertId)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBackClick) {
                Text("←")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Детали концерта",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.weight(1f)
            )
            if (uiState is ConcertDetailUiState.Success) {
                val concert = (uiState as ConcertDetailUiState.Success).concert
                TextButton(onClick = { viewModel.onEvent(ConcertDetailUiEvent.ToggleFavorite) }) {
                    Text(if (concert.isFavorite) "★" else "☆")
                }
            }
        }

        when (val state = uiState) {
            is ConcertDetailUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is ConcertDetailUiState.Success -> {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(state.concert.artist, style = MaterialTheme.typography.headlineLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Место: ${state.concert.venue}")
                    Text("Город: ${state.concert.city}")
                    Text("Дата: ${state.concert.date}")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(state.concert.description)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        "Цена: ${state.concert.price} ₽",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { onBuyClick(state.concert.id) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Купить билет")
                    }
                }
            }

            is ConcertDetailUiState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(state.message, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
