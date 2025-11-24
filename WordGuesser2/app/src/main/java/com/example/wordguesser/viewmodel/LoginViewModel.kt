package com.example.wordguesser.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordguesser.ui.theme.navigation.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var errorMessage by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)


    fun login(onLoginSuccess: () -> Unit) {
        viewModelScope.launch {
            if (isValidInput()) {
                isLoading = true
                errorMessage = null

                delay(2000)

                isLoading = false

                // Validación "dummy" para pruebas
                if (email.isNotEmpty() && password.length >= 4) {
                    onLoginSuccess()
                } else {
                    errorMessage = "Credenciales incorrectas (prueba cualquier email y pass > 4 caracteres)"
                }
            }
        }
    }

    private fun isValidInput(): Boolean {
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Por favor, llena todos los campos."
            return false
        }
        return true
    }
}