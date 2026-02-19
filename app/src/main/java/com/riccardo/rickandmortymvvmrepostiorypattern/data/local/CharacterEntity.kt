package com.riccardo.rickandmortymvvmrepostiorypattern.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.riccardo.rickandmortymvvmrepostiorypattern.domain.model.Character

/**
 * CharacterEntity: Rappresenta la tabella "characters" nel nostro database locale (Room).
 *
 * PERCHÉ USIAMO UN ENTITY?
 * 1. Offline Mode: Permette di visualizzare i dati anche se non c'è internet (usiamo il database locale).
 * 2. Caching: Evita di rifare chiamate di rete costose ogni volta che l'utente apre l'app.
 * 3. Persistenza: I dati rimangono salvati anche se l'app viene chiusa.
 *
 * @Entity: Definisce una tabella nel database locale.
 */
@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: Int, // L'ID univoco del personaggio è la chiave primaria della tabella
    val name: String,
    val status: String,
    val species: String,
    val imageUrl: String
)

/**
 * Extension Function: Converte un CharacterEntity (Dato Database) in un Character (Dato Domain).
 * Usiamo questa funzione nel Repository per trasformare i dati grezzi in dati pronti per l'UI.
 */
fun CharacterEntity.toDomain(): Character {
    return Character(
        id = id,
        name = name,
        status = status,
        species = species,
        imageUrl = imageUrl
    )
}

/**
 * Extension Function: Converte un Character (Dato Domain) in un CharacterEntity (Dato Database).
 * Usiamo questa funzione se dovessimo salvare dati locali generati dall'utente (in questo caso non serve ma è buona pratica).
 */
fun Character.toEntity(): CharacterEntity {
    return CharacterEntity(
        id = id,
        name = name,
        status = status,
        species = species,
        imageUrl = imageUrl
    )
}
