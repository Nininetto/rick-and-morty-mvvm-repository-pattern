package com.riccardo.rickandmortymvvmrepostiorypattern.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.riccardo.rickandmortymvvmrepostiorypattern.domain.model.Character

/**
 * CharactersScreen: Schermata principale dell'app. Mostra la lista dei personaggi.
 * Usa il ViewModel per ottenere i dati e reagisce ai cambiamenti di stato.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScreen(
    viewModel: CharactersViewModel = hiltViewModel(), // Hilt fornisce automaticamente il ViewModel.
    onCharacterClick: (Int) -> Unit // Funzione lambda per gestire il click sull'elemento (va al dettaglio).
) {
    // Trasforma lo StateFlow in uno State di Compose. Ogni volta che lo stato cambia, Compose ricalcola l'UI!
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Rick and Morty Characters") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            // Gestione dei diversi stati dell'UI definiti nel ViewModel.
            when (val state = uiState) {
                is CharactersUiState.Loading -> {
                    // Stato Caricamento: mostra un cerchio di caricamento.
                    CircularProgressIndicator()
                }
                is CharactersUiState.Success -> {
                    // Stato Successo: mostra la lista usando una LazyColumn (equivalente a RecyclerView).
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.characters) { character ->
                            CharacterItem(character, onCharacterClick)
                        }
                    }
                }
                is CharactersUiState.Error -> {
                    // Stato Errore: mostra un messaggio di errore all'utente.
                    Text(
                        text = "Errore: ${state.message}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

/**
 * CharacterItem: Singolo elemento della lista.
 */
@Composable
fun CharacterItem(character: Character, onClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(character.id) }, // Naviga al dettaglio quando cliccato.
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // AsyncImage (Coil): Scarica e mostra l'immagine in modo efficiente e asincrono.
            AsyncImage(
                model = character.imageUrl,
                contentDescription = character.name,
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Crop // Ritaglia l'immagine per riempire il quadrato.
            )
            Column {
                Text(text = character.name, style = MaterialTheme.typography.titleMedium)
                Text(text = "${character.status} - ${character.species}", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
