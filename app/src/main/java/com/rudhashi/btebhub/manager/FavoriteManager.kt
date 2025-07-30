package com.rudhashi.btebhub.manager

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rudhashi.btebhub.model.Favorite

class FavoriteManager(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)

    fun addFavorite(favorite: Favorite) {
        val favorites = getFavorites().toMutableList()
        favorites.add(favorite)
        saveFavorites(favorites)
    }

    fun removeFavorite(rollNumber: String) {
        val favorites = getFavorites().toMutableList()
        favorites.removeAll { it.rollNoOrRollComb == rollNumber }
        saveFavorites(favorites)
    }

    fun getFavorites(): List<Favorite> {
        val json = sharedPreferences.getString("favorites", "[]")
        val type = object : TypeToken<List<Favorite>>() {}.type
        val favorites: List<Favorite> = Gson().fromJson(json, type)

        // Sort by timestamp, latest added favorite will be first
        return favorites.sortedByDescending { it.timestamp }
    }

    fun isFavorite(rollNumber: String): Boolean {
        return getFavorites().any { it.rollNoOrRollComb == rollNumber }
    }

    private fun saveFavorites(favorites: List<Favorite>) {
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(favorites)
        editor.putString("favorites", json)
        editor.apply()
    }
}