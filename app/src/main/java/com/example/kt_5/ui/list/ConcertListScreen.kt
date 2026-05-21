package com.example.kt_5.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kt_5.data.local.entity.ConcertEntity

@Composable
fun ConcertListScreen(
    viewModel: ConcertListViewModel,
    onConcertClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(ConcertListUiEvent.LoadConcerts)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Афиша концертов",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(16.dp)
        )

        when (val state = uiState) {
            is ConcertListUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ConcertListUiState.Success -> {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.concerts, key = { it.id }) { concert ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onConcertClick(concert.id) }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = concert.artist,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = concert.venue,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = concert.date,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "${concert.price} ₽",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }

            is ConcertListUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.onEvent(ConcertListUiEvent.RetryLoad) }) {
                        Text("Повторить")
                    }
                }
            }

            is ConcertListUiState.Empty -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Нет концертов")
                }
            }
        }
    }
}