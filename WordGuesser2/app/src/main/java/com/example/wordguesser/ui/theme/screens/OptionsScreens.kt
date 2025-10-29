package com.example.wordguesser.ui.theme.screens

import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OptionsScreen(prefs: SharedPreferences, onMusicToggle: (Boolean) -> Unit) {

    // 1. Lee el valor guardado, o usa 'true' (encendido) si no hay nada.
    var isMusicOn by remember {
        mutableStateOf(prefs.getBoolean("music_on", true))
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Opciones", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Música de Fondo")
            Switch(
                checked = isMusicOn,
                onCheckedChange = { nuevoValor ->
                    isMusicOn = nuevoValor
                    // 2. Guarda el nuevo valor en SharedPreferences
                    prefs.edit().putBoolean("music_on", nuevoValor).apply()
                    // 3. Llama a la función para que el MediaPlayer reaccione
                    onMusicToggle(nuevoValor)
                }
            )
        }
    }
}