package com.riccardo.rickandmortymvvmrepostiorypattern

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.riccardo.rickandmortymvvmrepostiorypattern.ui.navigation.NavGraph
import com.riccardo.rickandmortymvvmrepostiorypattern.ui.theme.RickAndMortyMVVMRepositoryPatternTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * MainActivity: L'unica Activity dell'applicazione (Single Activity Architecture).
 * Tutte le schermate (Screens) saranno Composable caricati all'interno di questa activity.
 *
 * @AndroidEntryPoint: Istruisce Hilt a iniettare dipendenze in questa Activity (come i ViewModel).
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        /**
         * Abilita il disegno a tutto schermo, inclusa la zona sotto la barra di stato
         * e la barra di navigazione inferiore.
         */
        enableEdgeToEdge()
        
        /**
         * setContent: Definisce l'interfaccia utente dell'Activity usando Jetpack Compose.
         */
        setContent {
            // Applica il tema globale dell'app (colori, font, forme).
            RickAndMortyMVVMRepositoryPatternTheme {
                // Avvia il grafo di navigazione che gestisce il passaggio tra le schermate.
                NavGraph()
            }
        }
    }
}
