package com.riccardo.rickandmortymvvmrepostiorypattern.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * CharacterDao (Data Access Object): Contiene le query SQL necessarie per interagire con il database Room.
 * Room tradurrà queste funzioni in query SQL durante la compilazione.
 */
@Dao
interface CharacterDao {

    /**
     * Inserisce una lista di personaggi nel database locale.
     * @param OnConflictStrategy.REPLACE: Se un personaggio con lo stesso ID esiste già, sovrascrivilo.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterEntity>)

    /**
     * Recupera tutti i personaggi salvati nel database.
     * @return Flow<List<CharacterEntity>>: Restituisce un flusso di dati.
     *         Ogni volta che il database cambia, il Flow emette automaticamente la nuova lista!
     *         Questo ci permette di avere l'UI sempre aggiornata senza ricaricare manualmente.
     */
    @Query("SELECT * FROM characters")
    fun getAllCharacters(): Flow<List<CharacterEntity>>

    /**
     * Recupera un singolo personaggio tramite ID.
     */
    @Query("SELECT * FROM characters WHERE id = :id")
    fun getCharacterById(id: Int): Flow<CharacterEntity?>

    /**
     * Pulisce l'intero database. Utile per implementare un "Svuota Cache".
     */
    @Query("DELETE FROM characters")
    suspend fun clearAll()
}
