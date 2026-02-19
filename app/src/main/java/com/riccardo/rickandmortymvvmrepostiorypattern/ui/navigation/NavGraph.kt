package com.riccardo.rickandmortymvvmrepostiorypattern.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.riccardo.rickandmortymvvmrepostiorypattern.ui.screens.CharacterDetailScreen
import com.riccardo.rickandmortymvvmrepostiorypattern.ui.screens.CharactersScreen

/**
 * NavGraph: Definisce tutte le rotte (schermate) dell'app e come navigare tra di esse.
 * Utilizziamo Jetpack Compose Navigation.
 */
@Composable
fun NavGraph() {
    // Il NavController è l'oggetto centrale per navigare.
    val navController = rememberNavController()

    // NavHost definisce dove avvengono i cambi di schermata.
    // 'startDestination' indica la schermata iniziale ("characters").
    NavHost(navController = navController, startDestination = "characters") {
        
        // Rotta per la lista dei personaggi.
        composable("characters") {
            CharactersScreen(
                onCharacterClick = { characterId ->
                    // Quando un personaggio è cliccato, ordiniamo al controller di navigare al dettaglio.
                    navController.navigate("character/$characterId")
                }
            )
        }
        
        // Rotta per il dettaglio.
        // {characterId} indica un parametro variabile nell'URL della rotta.
        composable(
            route = "character/{characterId}",
            arguments = listOf(
                navArgument("characterId") { 
                    type = NavType.StringType // Definiamo il tipo di parametro atteso.
                }
            )
        ) {
            CharacterDetailScreen(
                onBackClick = { 
                    // Quando si clicca indietro, rimuoviamo l'ultima schermata dallo stack.
                    navController.popBackStack() 
                }
            )
        }
    }
}
