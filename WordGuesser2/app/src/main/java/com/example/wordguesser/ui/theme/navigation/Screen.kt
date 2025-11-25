package com.example.wordguesser.ui.theme.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login_screen")
    object Menu : Screen("menu_screen")
    object Game : Screen("game_screen")
    object Options : Screen("options_screen")
    object Register : Screen("register_screen")
}