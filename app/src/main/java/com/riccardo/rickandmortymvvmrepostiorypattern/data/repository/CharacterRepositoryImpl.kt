package com.riccardo.rickandmortymvvmrepostiorypattern.data.repository

import com.riccardo.rickandmortymvvmrepostiorypattern.data.local.CharacterDao
import com.riccardo.rickandmortymvvmrepostiorypattern.data.local.CharacterEntity
import com.riccardo.rickandmortymvvmrepostiorypattern.data.local.toDomain
import com.riccardo.rickandmortymvvmrepostiorypattern.data.remote.RickAndMortyApi
import com.riccardo.rickandmortymvvmrepostiorypattern.domain.model.Character
import com.riccardo.rickandmortymvvmrepostiorypattern.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * CharacterRepositoryImpl: L'implementazione reale del repository.
 * Gestisce la logica di coordinamento tra il database locale (Room) e le API remote (Retrofit).
 *
 * @Inject: Hilt inietterà automaticamente l'API e il DAO necessari nel costruttore.
 */
class CharacterRepositoryImpl @Inject constructor(
    private val api: RickAndMortyApi,
    private val dao: CharacterDao
) : CharacterRepository {

    /**
     * Trasforma il Flow di CharacterEntity (Database) in un Flow di Character (Domain).
     * Usiamo l'operatore 'map' per convertire ogni lista che arriva dal DB.
     */
    override val characters: Flow<List<Character>> = dao.getAllCharacters().map { entities ->
        entities.map { it.toDomain() }
    }

    /**
     * Recupera un singolo personaggio dal DB locale come flusso.
     */
    override fun getCharacterFlow(id: Int): Flow<Character?> = dao.getCharacterById(id).map { it?.toDomain() }

    /**
     * refreshCharacters: LA LOGICA DI CACHING.
     * 1. Fa la chiamata alle API.
     * 2. Se ha successo, trasforma i DTO in Entity.
     * 3. Salva le Entity nel database locale.
     *
     * In questo modo, l'app mostra sempre ciò che c'è nel DB locale e lo aggiorna non appena arrivano dati nuovi.
     */
    override suspend fun refreshCharacters(page: Int): Result<Unit> {
        return try {
            val response = api.getCharacters(page)
            val entities = response.results.map { dto ->
                CharacterEntity(
                    id = dto.id,
                    name = dto.name,
                    status = dto.status,
                    species = dto.species,
                    imageUrl = dto.image
                )
            }
            // Salvataggio nel database locale. Questo farà scattare automaticamente i Flow
            // che le schermate stanno osservando!
            dao.insertAll(entities)
            Result.success(Unit)
        } catch (e: Exception) {
            // Se non c'è internet, la chiamata fallisce qui, ma l'app continuerà a mostrare
            // i vecchi dati salvati nel database locale.
            Result.failure(e)
        }
    }
}
