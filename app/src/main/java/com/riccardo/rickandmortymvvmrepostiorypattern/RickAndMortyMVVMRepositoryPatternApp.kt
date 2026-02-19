package com.riccardo.rickandmortymvvmrepostiorypattern

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * RickAndMortyMVVMRepositoryPatternApp: La classe Application personalizzata dell'app.
 * È il punto di ingresso dell'intero processo dell'applicazione.
 *
 * @HiltAndroidApp: QUESTA ANNOTAZIONE È FONDAMENTALE.
 * Istruisce Hilt a generare i componenti necessari per la Dependency Injection (DI)
 * a livello di intera applicazione. Senza questa, Hilt non può funzionare.
 */
@HiltAndroidApp
class RickAndMortyMVVMRepositoryPatternApp : Application()
