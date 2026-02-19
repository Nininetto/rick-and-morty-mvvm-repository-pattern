package com.riccardo.rickandmortymvvmrepostiorypattern.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riccardo.rickandmortymvvmrepostiorypattern.domain.model.Character
import com.riccardo.rickandmortymvvmrepostiorypattern.domain.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * CharactersUiState: Rappresenta tutti i possibili stati della schermata dei personaggi.
 * Questo approccio (State-Driven UI) garantisce che la schermata mostri sempre
 * l'unica versione corretta dei dati.
 */
sealed class CharactersUiState {
    object Loading : CharactersUiState() // Sta caricando per la prima volta (nessun dato locale)
    data class Success(val characters: List<Character>) : CharactersUiState() // Abbiamo dati da mostrare
    data class Error(val message: String) : CharactersUiState() // Errore critico (niente internet e niente dati locali)
}

/**
 * CharactersViewModel: Il cervello della schermata dei personaggi.
 * Gestisce il caricamento dei dati, mantiene lo stato e lo espone all'UI.
 */
@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val repository: CharacterRepository
) : ViewModel() {

    // _uiState: Stato interno scrivibile (MutableStateFlow)
    private val _uiState = MutableStateFlow<CharactersUiState>(CharactersUiState.Loading)
    // uiState: Stato pubblico di sola lettura (StateFlow)
    val uiState: StateFlow<CharactersUiState> = _uiState.asStateFlow()

    init {
        /**
         * 1. OSSERVA IL DATABASE LOCALE (SSOT: Single Source of Truth)
         * Usiamo 'collectLatest' sul Flow del repository. Ogni volta che il database
         * cambia (es. inseriamo nuovi dati), questo blocco viene eseguito
         * e l'UI si aggiorna istantaneamente con lo stato 'Success'.
         */
        viewModelScope.launch {
            repository.characters.collectLatest { characters ->
                if (characters.isNotEmpty()) {
                    _uiState.value = CharactersUiState.Success(characters)
                }
            }
        }
        
        /**
         * 2. AVVIA L'AGGIORNAMENTO DALLE API
         */
        refresh()
    }

    /**
     * refresh: Tenta di scaricare dati freschi dalle API.
     */
    private fun refresh() {
        viewModelScope.launch {
            // Se non abbiamo ancora dati mostrati (non siamo in Success), mostriamo il caricamento.
            if (_uiState.value !is CharactersUiState.Success) {
                _uiState.value = CharactersUiState.Loading
            }
            
            // Proviamo l'aggiornamento
            repository.refreshCharacters(1)
                .onFailure { exception ->
                    // Se fallisce (es. niente internet) E non abbiamo dati locali, allora mostriamo l'errore.
                    if (_uiState.value !is CharactersUiState.Success) {
                        _uiState.value = CharactersUiState.Error(exception.message ?: "Errore di rete")
                    }
                }
        }
    }
}
