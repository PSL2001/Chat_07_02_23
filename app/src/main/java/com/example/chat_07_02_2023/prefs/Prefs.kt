package com.example.chat_07_02_2023.prefs

import android.content.Context

class Prefs(c: Context) {
    val storage = c.getSharedPreferences("superChat", 0)

    fun saveEmail(email: String) {
        storage.edit().putString("email", email).apply()
    }

    fun getEmail(): String? {
        return storage.getString("email", null)
    }

    fun deleteAll() {
        storage.edit().clear().apply()
    }
}