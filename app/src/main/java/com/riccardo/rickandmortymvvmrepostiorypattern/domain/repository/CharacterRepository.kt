package com.riccardo.rickandmortymvvmrepostiorypattern.domain.repository

import com.riccardo.rickandmortymvvmrepostiorypattern.domain.model.Character
import kotlinx.coroutines.flow.Flow

/**
 * Repository Interface: Definisce le azioni che l'app può compiere sui dati.
 * Non ci interessa sapere se i dati vengono da internet, da un file locale o da un DB.
 *
 * PERCHÉ USARE UN'INTERFACCIA?
 * 1. Testing: Possiamo creare un "finto" repository (Mock) per testare l'app senza internet.
 * 2. Manutenibilità: Se decidiamo di cambiare database, dobbiamo cambiare solo l'implementazione,
 *    mentre il resto dell'app continuerà a funzionare correttamente.
 */
interface CharacterRepository {
    /**
     * Flow<List<Character>>: Rappresenta il flusso continuo di personaggi.
     * Chiunque osserva questo campo vedrà i dati aggiornati ogni volta che cambiano nel database locale.
     */
    val characters: Flow<List<Character>>

    /**
     * Recupera un singolo personaggio tramite ID come flusso di dati.
     */
    fun getCharacterFlow(id: Int): Flow<Character?>

    /**
     * Aggiorna i dati locali scaricandoli dalle API.
     * @param page: La pagina da scaricare.
     * @return Result<Unit>: Indica se l'operazione è andata a buon fine o se c'è stato un errore.
     */
    suspend fun refreshCharacters(page: Int): Result<Unit>
}
