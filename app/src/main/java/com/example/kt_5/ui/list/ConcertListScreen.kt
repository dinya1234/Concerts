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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ConcertListScreen(
    viewModel: ConcertListViewModel,
    onConcertClick: (Int) -> Unit,
    onProfileClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(ConcertListUiEvent.LoadConcerts)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Афиша концертов",
                style = MaterialTheme.typography.headlineLarge
            )
            TextButton(onClick = onProfileClick) {
                Text("Кабинет")
            }
        }

        var cityFilter by remember { mutableStateOf("") }

        OutlinedTextField(
            value = cityFilter,
            onValueChange = {
                cityFilter = it
                viewModel.onEvent(ConcertListUiEvent.CityFilterChanged(it))
            },
            label = { Text("Фильтр по городу") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            singleLine = true
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
                if (state.offlineMessage != null) {
                    Text(
                        text = state.offlineMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

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
                                    text = concert.city,
                                    style = MaterialTheme.typography.bodyMedium
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
