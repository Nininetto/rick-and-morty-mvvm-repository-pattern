package com.riccardo.rickandmortymvvmrepostiorypattern.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * CharacterDto (Data Transfer Object)
 * Questa classe rappresenta esattamente la struttura del JSON che riceviamo dalle API di Rick and Morty.
 * Usiamo i DTO per separare ciò che arriva dalla rete da ciò che usiamo nell'interfaccia utente (Domain Model).
 *
 * @Serializable: Indica a Kotlinx Serialization che questa classe può essere convertita da/verso JSON.
 */
@Serializable
data class CharacterDto(
    @SerialName("id") val id: Int, // ID univoco del personaggio
    @SerialName("name") val name: String, // Nome del personaggio
    @SerialName("status") val status: String, // Stato (Alive, Dead, Unknown)
    @SerialName("species") val species: String, // Specie (Human, Alien, ecc.)
    @SerialName("type") val type: String, // Sottospecie o tipo particolare
    @SerialName("gender") val gender: String, // Genere
    @SerialName("origin") val origin: OriginDto, // Dati sulla provenienza
    @SerialName("location") val location: LocationDto, // Dati sulla posizione attuale
    @SerialName("image") val image: String, // URL dell'immagine del personaggio
    @SerialName("episode") val episode: List<String>, // Lista di URL degli episodi in cui compare
    @SerialName("url") val url: String, // URL del personaggio stesso nelle API
    @SerialName("created") val created: String // Data di creazione nel database delle API
)

@Serializable
data class OriginDto(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String
)

@Serializable
data class LocationDto(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String
)
