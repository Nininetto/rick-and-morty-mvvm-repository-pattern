package com.riccardo.rickandmortymvvmrepostiorypattern.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.riccardo.rickandmortymvvmrepostiorypattern.data.local.AppDatabase
import com.riccardo.rickandmortymvvmrepostiorypattern.data.local.CharacterDao
import com.riccardo.rickandmortymvvmrepostiorypattern.data.remote.RickAndMortyApi
import com.riccardo.rickandmortymvvmrepostiorypattern.data.repository.CharacterRepositoryImpl
import com.riccardo.rickandmortymvvmrepostiorypattern.domain.repository.CharacterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * AppModule: Il contenitore globale per le dipendenze dell'app (Hilt Module).
 * Qui istruiamo Hilt su come creare gli oggetti necessari (Retrofit, Room, Repository).
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Fornisce OkHttpClient per gestire le richieste HTTP (logging, timeout, ecc.).
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY // Mostra il JSON delle API nel Logcat per debug.
            })
            .build()
    }

    /**
     * Fornisce l'istanza di Retrofit configurata.
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val json = Json { ignoreUnknownKeys = true } // Ignora i campi JSON che non abbiamo definito nel DTO.
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType)) // Usa Kotlinx Serialization per il JSON.
            .build()
    }

    /**
     * Fornisce l'interfaccia API generata da Retrofit.
     */
    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): RickAndMortyApi {
        return retrofit.create(RickAndMortyApi::class.java)
    }

    /**
     * Fornisce il Database Room.
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "rick_morty.db" // Nome del file locale del database.
        ).build()
    }

    /**
     * Fornisce il DAO per accedere alle tabelle.
     */
    @Provides
    fun provideCharacterDao(db: AppDatabase): CharacterDao = db.characterDao

    /**
     * Fornisce il Repository.
     * Essendo un'interfaccia (CharacterRepository), dobbiamo dire a Hilt quale implementazione usare.
     */
    @Provides
    @Singleton
    fun provideRepository(api: RickAndMortyApi, dao: CharacterDao): CharacterRepository {
        return CharacterRepositoryImpl(api, dao)
    }
}
