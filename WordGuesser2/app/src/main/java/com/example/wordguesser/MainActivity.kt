package com.example.wordguesser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
// Imports MUY importantes para la navegación
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
// Imports de tus pantallas
import com.example.wordguesser.ui.theme.WordGuesserTheme
import com.example.wordguesser.ui.theme.screens.GameScreen
import com.example.wordguesser.ui.theme.screens.MenuScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WordGuesserTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 1. Creamos el controlador de navegación
                    val navController = rememberNavController()

                    // 2. NavHost es el "contenedor" que cambiará las pantallas
                    NavHost(
                        navController = navController,
                        startDestination = "menu" // Le decimos que empiece en el menú
                    ) {

                        // Ruta para la pantalla "menu"
                        composable("menu") {
                            MenuScreen(
                                onJugarClicked = {
                                    // Cuando se haga clic, navegar a "game"
                                    navController.navigate("game")
                                }
                            )
                        }

                        // Ruta para la pantalla "game"
                        composable("game") {
                            GameScreen() // El ViewModel se crea automáticamente
                        }
                    }
                }
            }
        }
    }
}