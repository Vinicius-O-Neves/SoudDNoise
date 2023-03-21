package com.example.navigation.features

import android.content.Context
import android.content.Intent

interface Feature {

    companion object {
        const val MAIN_PACKAGE = "com.example."
    }

    fun loadIntent(context: Context, destination: String): Intent? {
        return try {
            Intent(context, Class.forName(destination))
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            null
        }
    }
}