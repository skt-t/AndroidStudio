package com.example.wordguesser

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.wordguesser.ui.theme.WordGuesserTheme

// -------------------------------------------------------------------------
// 1. EL MODELO DE DATOS (NUESTRA LÓGICA)
// -------------------------------------------------------------------------
// NOTA: No tienes imágenes como "castillo.png" aún.
// Usaremos íconos genéricos de Android (android.R.drawable) como placeholders.
// ¡Tú podrás cambiar esto después por tus propias imágenes!
val desafios = mapOf(
    "anor londo" to R.drawable.anorlondo,
    "castillo" to R.drawable.castillo,
    "dolor" to R.drawable.neckhurt,
    "bigger" to R.drawable.bigger
)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WordGuesserTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GameScreen()
                }
            }
        }
    }
}

@Composable
fun GameScreen() {
    // -------------------------------------------------------------------------
    // 2. EL ESTADO DEL JUEGO
    // -------------------------------------------------------------------------
    // Guardamos la palabra secreta actual
    var palabraSecreta by remember { mutableStateOf("") }

    // Guardamos el ID de la imagen a mostrar (0 = ninguna imagen)
    var imagenAMostrar by remember { mutableIntStateOf(0) }

    // Guardamos el mensaje para el usuario
    var mensajeUsuario by remember { mutableStateOf("Pulsa el botón y adivina") }

    val context = LocalContext.current

    // -------------------------------------------------------------------------
    // 3. LÓGICA DE RECONOCIMIENTO DE VOZ
    // -------------------------------------------------------------------------

    // Este "Launcher" recibe el resultado del reconocimiento de voz
    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result : ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            // Obtenemos el texto que dijo el usuario
            val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val textoDicho = results?.get(0)?.lowercase() ?: ""

            // ¡AQUÍ ESTÁ LA LÓGICA DEL JUEGO!
            if (textoDicho == palabraSecreta) {
                // --- ¡CORRECTO! ---
                mensajeUsuario = "¡Excelente! Era '$palabraSecreta'"
                // Obtenemos la imagen usando nuestra palabra como clave
                imagenAMostrar = desafios[palabraSecreta] ?: 0
            } else {
                // --- INCORRECTO ---
                mensajeUsuario = "Dijiste '$textoDicho'. ¡Intenta de nuevo!"
            }
        }
    }

    // Esta función lanza el intent para que Google escuche
    fun iniciarReconocimientoVoz() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Di la palabra secreta...")
        }
        speechRecognizerLauncher.launch(intent)
    }

    // -------------------------------------------------------------------------
    // 4. LÓGICA DE PERMISOS (PARA EL MICRÓFONO)
    // -------------------------------------------------------------------------

    // Este "Launcher" pide el permiso de RECORD_AUDIO
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted : Boolean ->
        if (isGranted) {
            // Permiso concedido, ¡a escuchar!
            iniciarReconocimientoVoz()
        } else {
            // Permiso denegado
            mensajeUsuario = "Necesito permiso del micrófono para jugar."
        }
    }

    // Función para iniciar una nueva ronda
    fun nuevaRonda() {
        palabraSecreta = desafios.keys.random() // Elige palabra aleatoria
        imagenAMostrar = 0 // Oculta la imagen
        mensajeUsuario = "¿Cuál es la palabra?"
    }

    // Inicia la primera ronda cuando la app carga
    LaunchedEffect(Unit) {
        nuevaRonda()
    }


    // -------------------------------------------------------------------------
    // 5. LA INTERFAZ DE USUARIO (LA VISTA)
    // -------------------------------------------------------------------------
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Muestra el mensaje (ej. "¡Intenta de nuevo!")
        Text(
            text = mensajeUsuario,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Muestra la imagen SÓLO SI se ha adivinado la palabra
        if (imagenAMostrar != 0) {
            Image(
                painter = painterResource(id = imagenAMostrar),
                contentDescription = palabraSecreta,
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Botón para hablar
        Button(onClick = {
            // Comprueba el permiso ANTES de intentar escuchar
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) -> {
                    // Permiso ya concedido
                    iniciarReconocimientoVoz()
                }
                else -> {
                    // Pide el permiso
                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
            }
        }) {
            Text("Hablar")
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Botón para pasar a la siguiente palabra
        Button(onClick = { nuevaRonda() }) {
            Text("Siguiente Palabra")
        }
    }
}