package com.example.kt_5.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ConcertDetailScreen(
    viewModel: ConcertDetailViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(ConcertDetailUiEvent.LoadConcert)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBackClick) {
                Text("<-")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Детали концерта",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        when (val state = uiState) {
            is ConcertDetailUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ConcertDetailUiState.Success -> {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = state.concert.artist,
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Место: ${state.concert.venue}")
                    Text("Город: ${state.concert.city}")
                    Text("Дата: ${state.concert.date}")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(state.concert.description)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Цена: ${state.concert.price} ₽",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            is ConcertDetailUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}