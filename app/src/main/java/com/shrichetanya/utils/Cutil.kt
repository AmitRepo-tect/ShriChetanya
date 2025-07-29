package com.shrichetanya.utils

import android.content.Context

class Cutil {
    companion object {
        fun saveStringInSP(context: Context, key: String, value: String) {
            val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString(key, value)
                apply() // or commit()
            }
        }

        fun getStringFromSP(context: Context, key: String): String {
            val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            return sharedPref.getString(key, "")!! // null is the default if not found
        }

        fun saveBooleanInSP(context: Context, key: String, value: Boolean) {
            val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putBoolean(key, value)
                apply() // or commit()
            }
        }

        fun getBooleanFromSP(context: Context, key: String): Boolean {
            val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            return sharedPref.getBoolean(key, false) // null is the default if not found
        }

        fun clearAllPreferences(context: Context) {
            val prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            prefs.edit().clear().apply()
        }

    }
}