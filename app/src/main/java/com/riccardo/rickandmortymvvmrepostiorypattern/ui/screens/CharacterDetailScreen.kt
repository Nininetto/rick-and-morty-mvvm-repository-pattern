package com.riccardo.rickandmortymvvmrepostiorypattern.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

/**
 * CharacterDetailScreen: Mostra i dettagli di un singolo personaggio.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    viewModel: CharacterDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit // Funzione lambda per gestire il ritorno alla schermata precedente.
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dettaglio Personaggio") },
                navigationIcon = {
                    // Pulsante Back (Freccia Indietro) nella barra superiore.
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is CharacterDetailUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is CharacterDetailUiState.Success -> {
                    val character = state.character
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Immagine del personaggio caricata via Coil.
                        AsyncImage(
                            model = character.imageUrl,
                            contentDescription = character.name,
                            modifier = Modifier
                                .size(200.dp)
                                .padding(bottom = 16.dp),
                            contentScale = ContentScale.Crop
                        )
                        // Informazioni dettagliate.
                        Text(text = character.name, style = MaterialTheme.typography.headlineMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Stato: ${character.status}", style = MaterialTheme.typography.bodyLarge)
                        Text(text = "Specie: ${character.species}", style = MaterialTheme.typography.bodyLarge)
                    }
                }
                is CharacterDetailUiState.Error -> {
                    Text(
                        text = "Errore: ${state.message}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
