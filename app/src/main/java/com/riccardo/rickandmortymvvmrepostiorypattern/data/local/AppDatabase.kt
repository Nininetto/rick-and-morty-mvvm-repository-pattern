package com.riccardo.rickandmortymvvmrepostiorypattern.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * AppDatabase: Il cuore del database Room. Definisce la versione, le tabelle (entities)
 * e i punti di accesso ai dati (DAOs).
 *
 * @Database: Specifica quali tabelle compongono il database.
 * version = 1: Versione iniziale. Se cambi la struttura (es. aggiungi un campo), dovrai aumentare la versione e gestire le migrazioni.
 * exportSchema = false: Non esportiamo lo schema JSON (usato per tracking delle versioni).
 */
@Database(entities = [CharacterEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    // Definendo questa funzione astratta, Room saprà come creare l'istanza del DAO per noi.
    abstract val characterDao: CharacterDao
}
