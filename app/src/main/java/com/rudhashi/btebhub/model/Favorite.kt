package com.rudhashi.btebhub.model

data class Favorite(
    val name: String,      // New field to store Name
    val rollNoOrRollComb: String,
    val institute: String,
    val semester: String,
    val regulation: String,
    val timestamp: Long, // Add timestamp field
    val isSingle: Boolean
)