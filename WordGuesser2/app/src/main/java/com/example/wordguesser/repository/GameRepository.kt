package com.example.wordguesser.repository

import com.example.wordguesser.model.Desafio
import com.example.wordguesser.model.listaDeDesafios

class GameRepository {
    fun obtenerDesafios(): List<Desafio> {
        return listaDeDesafios
    }

    fun obtenerDesafioAleatorio(): Desafio {
        return listaDeDesafios.random()
    }
}