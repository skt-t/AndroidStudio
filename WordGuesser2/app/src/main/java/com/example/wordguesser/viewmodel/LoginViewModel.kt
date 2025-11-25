package com.example.wordguesser.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordguesser.model.User
import com.example.wordguesser.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    // Changed from "email" to "username" to match your backend
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var errorMessage by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)

    // Create instance of AuthRepository
    private val repository = AuthRepository()

    fun login(onLoginSuccess: () -> Unit) {
        viewModelScope.launch {
            if (isValidInput()) {
                isLoading = true
                errorMessage = null

                // Create User object - send the input as both username AND email
                // The backend will check both fields
                val user = User(
                    username = username,
                    password = password,
                    email = username  // Send same value as email too
                )

                // Debug: Print what we're sending
                println("=== LOGIN DEBUG ===")
                println("Username: ${user.username}")
                println("Password: ${user.password}")
                println("==================")

                // Call the backend API
                val result = repository.login(user)

                isLoading = false

                if (result.isSuccess) {
                    // Login successful!
                    println("✅ Login SUCCESS")
                    onLoginSuccess()
                } else {
                    // Login failed - show error
                    println("❌ Login FAILED: ${result.exceptionOrNull()?.message}")
                    errorMessage = result.exceptionOrNull()?.message ?: "Error al iniciar sesión"
                }
            }
        }
    }

    private fun isValidInput(): Boolean {
        if (username.isBlank() || password.isBlank()) {
            errorMessage = "Por favor, llena todos los campos."
            return false
        }
        return true
    }
}
