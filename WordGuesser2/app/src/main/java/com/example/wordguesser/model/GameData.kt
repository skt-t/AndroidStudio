package com.example.wordguesser

// ¡Importante! Asegúrate de que este import SÍ aparezca
// y no esté en gris después de un "Rebuild"
import com.example.wordguesser.R

data class Desafio(
    val palabraSecreta: String,
    val pistaTexto: String,
    val pistaImagen: Int,
    val imagenRespuesta: Int
)

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
        pistaTexto = "P _ _ _ _ O",
        pistaImagen = R.drawable.payaso_pista,
        imagenRespuesta = R.drawable.payaso
    ),
    Desafio(
        palabraSecreta = "resident evil 4",
        pistaTexto = "R _ S _ D _ _ T   _ _ _ L   _",
        pistaImagen = R.drawable.resident_evil_pista,
        imagenRespuesta = R.drawable.resident_evil_4
    ),
    Desafio(
        palabraSecreta = "digger",
        pistaTexto = "_ _ _ G E R",
        pistaImagen = R.drawable.digger_pista,
        imagenRespuesta = R.drawable.digger_pista
    ),
    Desafio(
        palabraSecreta = "finger",
        pistaTexto = "_ I N _ _ R",
        pistaImagen = R.drawable.finger_pista,
        imagenRespuesta = R.drawable.finger_pista
    ),
    Desafio(
        palabraSecreta = "singer",
        pistaTexto = "_ I N G _ _",
        pistaImagen = R.drawable.singer_pista,
        imagenRespuesta = R.drawable.singer_pista
    ),
    Desafio(
        palabraSecreta = "halloween",
        pistaTexto = "_ _ L _ _ _ W _ _ N",
        pistaImagen = R.drawable.halloween_pista,
        imagenRespuesta = R.drawable.halloween_pista
    ),
    Desafio(
        palabraSecreta = "hitler",
        pistaTexto = "_ _ _ _ _ _",
        pistaImagen = R.drawable.htl_pista,
        imagenRespuesta = R.drawable.htl
    ),
    Desafio(
        palabraSecreta = "playa",
        pistaTexto = "_ _ A _ _",
        pistaImagen = R.drawable.playa_pista,
        imagenRespuesta = R.drawable.playa_pista
    )
)

