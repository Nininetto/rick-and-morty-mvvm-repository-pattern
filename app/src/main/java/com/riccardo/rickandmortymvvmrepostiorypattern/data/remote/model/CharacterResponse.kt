package com.riccardo.rickandmortymvvmrepostiorypattern.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * CharacterResponse: Questa è la classe "contenitore" per la risposta delle API di Rick and Morty.
 * Spesso le API non restituiscono direttamente una lista, ma un oggetto che contiene informazioni
 * di paginazione (info) e i dati veri e propri (results).
 */
@Serializable
data class CharacterResponse(
    @SerialName("info") val info: InfoDto, // Info sulla paginazione (quante pagine totali, URL per la prossima pagina, ecc.)
    @SerialName("results") val results: List<CharacterDto> // La lista dei personaggi effettivi
)

@Serializable
data class InfoDto(
    @SerialName("count") val count: Int, // Numero totale di personaggi presenti nelle API
    @SerialName("pages") val pages: Int, // Numero totale di pagine disponibili
    @SerialName("next") val next: String?, // URL della pagina successiva (null se siamo all'ultima)
    @SerialName("prev") val prev: String? // URL della pagina precedente (null se siamo alla prima)
)
