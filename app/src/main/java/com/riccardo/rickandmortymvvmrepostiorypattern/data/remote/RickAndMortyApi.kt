package com.riccardo.rickandmortymvvmrepostiorypattern.data.remote

import com.riccardo.rickandmortymvvmrepostiorypattern.data.remote.model.CharacterDto
import com.riccardo.rickandmortymvvmrepostiorypattern.data.remote.model.CharacterResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * RickAndMortyApi: L'interfaccia usata da Retrofit per definire le chiamate di rete.
 * Non dobbiamo implementare noi la logica di rete; Retrofit lo farà per noi.
 *
 * @GET("character"): Definisce il tipo di richiesta HTTP e l'endpoint relativo.
 */
interface RickAndMortyApi {

    /**
     * Recupera una lista di personaggi paginata.
     * @param page: Il numero della pagina da richiedere (di default la 1).
     * @return CharacterResponse: L'oggetto che contiene le info di paginazione e i personaggi.
     */
    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int = 1): CharacterResponse

    /**
     * Recupera i dettagli di un singolo personaggio tramite il suo ID.
     * @param id: L'id del personaggio da cercare.
     * @return CharacterDto: I dati completi del personaggio.
     */
    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): CharacterDto
}
