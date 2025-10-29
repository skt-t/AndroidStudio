package com.example.wordguesser.ui.theme.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wordguesser.GameViewModel

// ¡Esta es la pantalla del juego, pero ahora es "limpia"!
// Recibe el ViewModel (cerebro)
@Composable
fun GameScreen(gameViewModel: GameViewModel = viewModel()) {

    val uiState = gameViewModel.uiState
    val context = LocalContext.current

    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val textoDicho = results?.get(0)?.lowercase() ?: ""
            gameViewModel.comprobarRespuesta(textoDicho)
        }
    }

    fun iniciarReconocimientoVoz() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Di la palabra secreta...")
        }
        speechRecognizerLauncher.launch(intent)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            iniciarReconocimientoVoz()
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        uiState.desafioActual?.let { desafio ->
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = uiState.mensajeUsuario,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = if (uiState.estaResuelto) desafio.palabraSecreta else desafio.pistaTexto,
                    style = MaterialTheme.typography.displaySmall,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 4.sp
                )
                Spacer(modifier = Modifier.height(20.dp))

                // --- ¡AQUÍ ESTÁ LA ANIMACIÓN! ---
                // Esta caja aparecerá/desaparecerá con un fundido
                AnimatedVisibility(
                    visible = !uiState.estaResuelto,
                    enter = fadeIn(), exit = fadeOut()
                ) {
                    // Ponemos el Box ADENTRO para mantener el tamaño
                    Box(modifier = Modifier.size(200.dp), contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = desafio.pistaImagen),
                            contentDescription = "Pista",
                            modifier = Modifier.size(150.dp)
                        )
                    }
                }

                AnimatedVisibility(
                    visible = uiState.estaResuelto,
                    enter = fadeIn(), exit = fadeOut()
                ) {
                    // Y lo ponemos ADENTRO aquí también
                    Box(modifier = Modifier.size(200.dp), contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = desafio.imagenRespuesta),
                            contentDescription = desafio.palabraSecreta,
                            modifier = Modifier.size(200.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))

                Button(onClick = {
                    when (PackageManager.PERMISSION_GRANTED) {
                        ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) -> {
                            iniciarReconocimientoVoz()
                        }
                        else -> {
                            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        }
                    }
                }) {
                    Text("Hablar")
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(onClick = { gameViewModel.nuevaRonda() }) {
                    Text("Siguiente Palabra")
                }
            }
        }
    }
}