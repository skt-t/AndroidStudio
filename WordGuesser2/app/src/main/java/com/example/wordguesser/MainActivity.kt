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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.wordguesser.ui.theme.WordGuesserTheme

// -------------------------------------------------------------------------
// 1. EL MODELO DE DATOS (NUESTRA LÓGICA)
// -------------------------------------------------------------------------
// NOTA: No tienes imágenes como "castillo.png" aún.
// Usaremos íconos genéricos de Android (android.R.drawable) como placeholders.
// ¡Tú podrás cambiar esto después por tus propias imágenes!
// ... (tus imports)


data class Desafio(
    val palabraSecreta: String,   // Ej: "castillo"
    val pistaTexto: String,       // Ej: "_ _ S T _ L L _"
    val pistaImagen: Int,         // Ej: R.drawable.castillo_pista
    val imagenRespuesta: Int      // Ej: R.drawable.castillo_completo
)

// ... (justo después del data class)


val listaDeDesafios = listOf(
    Desafio(
        palabraSecreta = "anor londo",
        pistaTexto = "A _ O R _ _ N _ O",
        pistaImagen = R.drawable.castillo, // <-- Imagen de guía
        imagenRespuesta = R.drawable.anorlondo // <-- Imagen de respuesta
    ),
    Desafio(
        palabraSecreta = "bigger",
        pistaTexto = "_ I _ G _ R",
        pistaImagen = R.drawable.bigger_pista,
        imagenRespuesta = R.drawable.bigger
    ),
    Desafio(
        palabraSecreta = "payaso",
        pistaTexto = "P _ _ _ O",
        pistaImagen = R.drawable.payaso_pista,
        imagenRespuesta = R.drawable.payaso
    ),
    Desafio(
        palabraSecreta = "resident evil 4",
        pistaTexto = "R _ S _ _ D _ _ T   _ _ _ L   _",
        pistaImagen = R.drawable.resident_evil_pista,
        imagenRespuesta = R.drawable.resident_evil_4
    )
    // ... añade más Desafios aquí
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
    // 2. EL ESTADO DEL JUEGO (ACTUALIZADO)
    // -------------------------------------------------------------------------
    // Guardamos el desafío completo actual, no solo la palabra
    var desafioActual by remember { mutableStateOf<Desafio?>(null) } // <-- CAMBIO

    // Un booleano para saber si mostramos la respuesta
    var estaResuelto by remember { mutableStateOf(false) } // <-- CAMBIO

    // Guardamos el mensaje para el usuario
    var mensajeUsuario by remember { mutableStateOf("Cargando...") }

    val context = LocalContext.current

    // 3. LÓGICA DE RECONOCIMIENTO DE VOZ (ACTUALIZADA)
    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val textoDicho = results?.get(0)?.lowercase() ?: ""

            // ¡AQUÍ ESTÁ LA LÓGICA DEL JUEGO (ACTUALIZADA)!
            if (textoDicho == desafioActual?.palabraSecreta) { // <-- CAMBIO
                // --- ¡CORRECTO! ---
                mensajeUsuario = "¡Excelente! Era '${desafioActual?.palabraSecreta}'"
                estaResuelto = true // <-- CAMBIO
            } else {
                // --- INCORRECTO ---
                mensajeUsuario = "Dijiste '$textoDicho'. ¡Intenta de nuevo!"
                estaResuelto = false // <-- CAMBIO
            }
        }
    }

    fun iniciarReconocimientoVoz() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Di la palabra secreta...")
        }
        speechRecognizerLauncher.launch(intent)
    }

    // 4. LÓGICA DE PERMISOS (Sin cambios)
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            iniciarReconocimientoVoz()
        } else {
            mensajeUsuario = "Necesito permiso del micrófono para jugar."
        }
    }

    // Función para iniciar una nueva ronda (ACTUALIZADA)
    fun nuevaRonda() {
        desafioActual = listaDeDesafios.random() // <-- CAMBIO
        estaResuelto = false // <-- CAMBIO
        mensajeUsuario = "Adivina la palabra:" // <-- CAMBIO
    }

    // Inicia la primera ronda cuando la app carga
    LaunchedEffect(Unit) {
        nuevaRonda()
    }


    // -------------------------------------------------------------------------
    // 5. LA INTERFAZ DE USUARIO (ACTUALIZADA)
    // -------------------------------------------------------------------------
    // Usamos un 'Box' para centrar si el desafío no ha cargado
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        // Solo muestra el juego si ya cargó un desafío
        desafioActual?.let { desafio -> // <-- CAMBIO

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

                // --- NUEVA PISTA DE TEXTO ---
                Text(
                    text = if (estaResuelto) desafio.palabraSecreta else desafio.pistaTexto, // <-- CAMBIO
                    style = MaterialTheme.typography.displaySmall,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 4.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // --- IMAGEN DE PISTA O RESPUESTA ---
                if (estaResuelto) {
                    // Muestra la IMAGEN DE RESPUESTA si se resolvió
                    Image(
                        painter = painterResource(id = desafio.imagenRespuesta), // <-- CAMBIO
                        contentDescription = desafio.palabraSecreta,
                        modifier = Modifier.size(200.dp)
                    )
                } else {
                    // Muestra la IMAGEN DE PISTA si NO se resolvió
                    Image(
                        painter = painterResource(id = desafio.pistaImagen), // <-- CAMBIO
                        contentDescription = "Pista",
                        modifier = Modifier.size(150.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Botón para hablar
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

                // Botón para pasar a la siguiente palabra
                Button(onClick = { nuevaRonda() }) {
                    Text("Siguiente Palabra")
                }
            }

        }
    }
}