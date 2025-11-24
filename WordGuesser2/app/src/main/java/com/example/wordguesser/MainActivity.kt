package com.example.wordguesser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wordguesser.ui.theme.WordGuesserTheme
import com.example.wordguesser.ui.theme.navigation.Screen
import com.example.wordguesser.ui.theme.screens.GameScreen
import com.example.wordguesser.ui.theme.screens.LoginScreen
import com.example.wordguesser.ui.theme.screens.MenuScreen
import com.example.wordguesser.ui.theme.screens.OptionsScreen
import com.example.wordguesser.ui.theme.screens.RegisterScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtenemos las preferencias para pasarlas a la pantalla de Opciones
        val prefs = getSharedPreferences("game_prefs", MODE_PRIVATE)

        setContent {
            WordGuesserTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 1. Creamos el controlador de navegación
                    val navController = rememberNavController()

                    // 2. Configuramos el NavHost usando las rutas seguras de Screen.kt
                    // IMPORTANTE: Cambiamos el inicio a Login
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Login.route
                    ) {

                        // --- PANTALLA DE LOGIN ---
                        composable(Screen.Login.route) {
                            LoginScreen(navController = navController)
                        }

                        composable(Screen.Register.route) {
                            RegisterScreen(navController = navController)
                        }

                        // --- PANTALLA DE MENÚ ---
                        composable(Screen.Menu.route) {
                            MenuScreen(
                                onJugarClicked = {
                                    navController.navigate(Screen.Game.route)
                                }
                                // Si en el futuro agregas un botón de opciones al menú:
                                // onOpcionesClicked = { navController.navigate(Screen.Options.route) }
                            )
                        }

                        // --- PANTALLA DE JUEGO ---
                        composable(Screen.Game.route) {
                            GameScreen()
                        }

                        // --- PANTALLA DE OPCIONES ---
                        composable(Screen.Options.route) {
                            OptionsScreen(prefs = prefs) { isMusicOn ->
                                // Aquí puedes manejar lógica global si la música cambia,
                                // aunque tu MenuScreen ya observa el ciclo de vida.
                            }
                        }
                    }
                }
            }
        }
    }
}