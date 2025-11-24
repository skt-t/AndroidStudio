package com.example.wordguesser.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
// ¡Estos imports deberían funcionar ahora!

data class GameUiState(
    val desafioActual: Desafio? = null,
    val mensajeUsuario: String = "Cargando...",
    val estaResuelto: Boolean = false
)

class GameViewModel : ViewModel() {

    var uiState by mutableStateOf(GameUiState())
        private set

    init {
        nuevaRonda()
    }

    fun nuevaRonda() {
        val nuevoDesafio = listaDeDesafios.random()
        uiState = GameUiState(
            desafioActual = nuevoDesafio,
            mensajeUsuario = "Adivina la palabra:",
            estaResuelto = false
        )
    }

    fun comprobarRespuesta(textoDicho: String) {
        // La solución de "Gemini de Android"

        val desafioActual = uiState.desafioActual

        if (desafioActual != null && textoDicho == desafioActual.palabraSecreta) {
            // --- ¡CORRECTO! ---
            uiState = uiState.copy(
                mensajeUsuario = "¡Excelente! Era '${desafioActual.palabraSecreta}'",
                estaResuelto = true
            )
        } else {
            // --- INCORRECTO ---
            uiState = uiState.copy(
                mensajeUsuario = "Dijiste '$textoDicho'. ¡Intenta de nuevo!"
            )
        }
    }


}