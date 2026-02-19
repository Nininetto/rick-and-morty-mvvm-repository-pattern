package com.riccardo.rickandmortymvvmrepostiorypattern.domain.model

/**
 * Domain Model: Character
 * Questa è la classe che l'App userà effettivamente per mostrare i dati a schermo.
 *
 * PERCHÉ USIAMO UN DOMAIN MODEL?
 * 1. Separazione: Se le API di Rick and Morty cambiassero i nomi dei campi JSON (es. "image" diventasse "photo_url"),
 *    dovremmo cambiare solo il DTO (CharacterDto) e non tutta l'App. Il Domain Model resta stabile.
 * 2. Pulizia: Contiene solo i dati che ci servono davvero per le nostre schermate, ignorando il resto
 *    (come ad esempio la lista di episodi in cui compare il personaggio, se non ci serve).
 */
data class Character(
    val id: Int, // ID univoco
    val name: String, // Nome da visualizzare
    val status: String, // Stato (Vivo, Morto, ...)
    val species: String, // Specie
    val imageUrl: String // L'URL dell'immagine da caricare
)
