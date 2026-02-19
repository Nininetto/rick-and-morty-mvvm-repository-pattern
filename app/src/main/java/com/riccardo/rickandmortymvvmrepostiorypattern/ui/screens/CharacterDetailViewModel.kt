package com.riccardo.rickandmortymvvmrepostiorypattern.ui.screens

import androidx.lifecycle.SavedStateHandle
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
 * Stati della schermata di dettaglio del personaggio.
 */
sealed class CharacterDetailUiState {
    object Loading : CharacterDetailUiState()
    data class Success(val character: Character) : CharacterDetailUiState()
    data class Error(val message: String) : CharacterDetailUiState()
}

/**
 * CharacterDetailViewModel: Gestisce lo stato della schermata di dettaglio.
 *
 * @SavedStateHandle: Hilt inietta automaticamente questo oggetto che contiene
 * i parametri di navigazione (in questo caso 'characterId').
 */
@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val repository: CharacterRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<CharacterDetailUiState>(CharacterDetailUiState.Loading)
    val uiState: StateFlow<CharacterDetailUiState> = _uiState.asStateFlow()

    init {
        // Recupera l'ID del personaggio passato durante la navigazione.
        val characterIdString = savedStateHandle.get<String>("characterId")
        val characterId = characterIdString?.toIntOrNull()
        
        if (characterId != null) {
            // Se l'ID è valido, inizia a osservare quel personaggio dal database locale.
            observeCharacter(characterId)
        } else {
            // Se l'ID è nullo, mostra un errore.
            _uiState.value = CharacterDetailUiState.Error("ID personaggio non valido")
        }
    }

    private fun observeCharacter(id: Int) {
        viewModelScope.launch {
            /**
             * Osserva il personaggio singolo nel database locale.
             * Dato che usiamo un Flow, se il personaggio viene aggiornato nel database
             * (magari da un refresh in background), questa schermata si aggiornerà da sola!
             */
            repository.getCharacterFlow(id).collectLatest { character ->
                if (character != null) {
                    _uiState.value = CharacterDetailUiState.Success(character)
                } else {
                    // Se non troviamo il personaggio nel database, mostriamo caricamento (potrebbe essere in arrivo).
                    _uiState.value = CharacterDetailUiState.Loading
                }
            }
        }
    }
}
