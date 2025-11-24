package com.example.wordguesser.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordguesser.model.User
import com.example.wordguesser.repository.AuthRepository
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    // Instanciamos el repositorio
    private val repository = AuthRepository()

    // Variables para guardar lo que el usuario escribe
    var username by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var statusMessage by mutableStateOf<String?>(null)

    fun register(onSuccess: () -> Unit) {
        // 1. Validación básica
        if (username.isBlank() || password.isBlank()) {
            statusMessage = "Por favor, completa usuario y contraseña."
            return
        }

        viewModelScope.launch {
            isLoading = true
            statusMessage = "Registrando..."

            // 2. Creamos el objeto User
            val newUser = User(
                username = username,
                email = email,
                password = password
            )

            // 3. Llamamos al Backend
            val result = repository.register(newUser)

            isLoading = false

            // 4. Verificamos el resultado
            if (result.isSuccess) {
                statusMessage = "¡Cuenta creada con éxito!"
                onSuccess()
            } else {
                statusMessage = "Error: ${result.exceptionOrNull()?.message}"
            }
        }
    }
}