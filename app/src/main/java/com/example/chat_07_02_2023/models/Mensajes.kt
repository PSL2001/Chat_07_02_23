package com.example.chat_07_02_2023.models

data class Mensajes(
    val fecha: Long = System.currentTimeMillis(),
    val mensaje: String? = null,
    val email: String? = null,
)
