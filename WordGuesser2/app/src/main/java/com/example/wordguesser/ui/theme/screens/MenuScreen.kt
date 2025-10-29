package com.example.wordguesser.ui.theme.screens

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
import com.example.wordguesser.R // Asegúrate de que importe tu R

@Composable
fun MenuScreen(
    // Esta es una "función lambda" que le pasaremos
    // para decirle qué hacer cuando se presione el botón.
    onJugarClicked: () -> Unit
) {
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

        // Usemos una de tus imágenes como "logo" temporal.
        // ¡Puedes cambiar `R.drawable.castillo` por la que quieras!
        Image(
            painter = painterResource(id = R.drawable.castillo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onJugarClicked, // Aquí llamamos a la acción
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .height(60.dp)
        ) {
            Text("¡JUGAR!", style = MaterialTheme.typography.titleLarge)
        }
    }
}