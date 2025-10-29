package com.example.wordguesser.ui.theme.screens

// --- IMPORTS NUEVOS ---
import android.media.MediaPlayer
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
// --- FIN IMPORTS NUEVOS ---

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.wordguesser.R

@Composable
fun MenuScreen(
    onJugarClicked: () -> Unit
) {
    // --- LÓGICA DE MÚSICA NUEVA ---

    // 1. Obtenemos el contexto para poder acceder a los archivos 'raw'
    val context = LocalContext.current

    // 2. Creamos y recordamos el MediaPlayer
    // CAMBIA 'R.raw.menu_music' por el nombre de tu archivo
    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.background_music).apply {
            isLooping = true // Para que la música se repita
        }
    }

    // 3. Obtenemos el "dueño" del ciclo de vida (la pantalla misma)
    val lifecycleOwner = LocalLifecycleOwner.current

    // 4. Este es el "controlador" mágico
    DisposableEffect(lifecycleOwner) {

        // Creamos un observador que reacciona a los cambios de estado
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                // Cuando la app/pantalla está activa (en primer plano)
                Lifecycle.Event.ON_RESUME -> {
                    mediaPlayer.start()
                }
                // Cuando la app/pantalla se va a segundo plano
                Lifecycle.Event.ON_PAUSE -> {
                    mediaPlayer.pause()
                }
                // No necesitamos ON_STOP o ON_DESTROY gracias a 'onDispose'
                else -> {}
            }
        }

        // Añadimos el observador
        lifecycleOwner.lifecycle.addObserver(observer)

        // Esta es la parte de "limpieza" (Dispose)
        // Se llama AUTOMÁTICAMENTE cuando dejas esta pantalla
        // (es decir, cuando navegas a 'GameScreen')
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mediaPlayer.stop() // Detiene la música
            mediaPlayer.release() // Libera los recursos de la memoria
        }
    }

    // --- FIN DE LÓGICA DE MÚSICA ---


    // Tu UI del menú (esto no cambia)
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Word Guesser",
            style = MaterialTheme.typography.displayLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))

        Image(
            painter = painterResource(id = R.drawable.castillo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onJugarClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .height(60.dp)
        ) {
            Text("¡JUGAR!", style = MaterialTheme.typography.titleLarge)
        }
    }
}