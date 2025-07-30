package com.rudhashi.btebhub.utils

import android.content.Context
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

object SmartData {
    // Dark Mode Theme
    const val PREF_KEY = "theme"
    const val SWITCH_BUTTON_KEY = "isNightMode"

    // Admob Ads Implementation
    const val ADMOB_KEY = "admob"
    const val VISIBILITY_KEY = "AdVisibility"

    // Splash Screen
    const val SPLASH_SCREEN: Long = 2000

    // Function to display Toast
    fun lToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun sToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun lSnackbar(viewGroup: ViewGroup, message: String) {
        Snackbar.make(viewGroup, message, Snackbar.LENGTH_LONG).show()
    }

    fun sSnackbar(viewGroup: ViewGroup, message: String) {
        Snackbar.make(viewGroup, message, Snackbar.LENGTH_SHORT).show()
    }

    fun dpToPx(context: Context, dp: Float): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }

    fun pxToDp(context: Context, px: Float): Float {
        val density = context.resources.displayMetrics.density
        return px / density
    }

    fun randomNumber(int: Int): Int {
        return (Math.random() * int).toInt()
    }

    fun RecyclerView.setHeightBasedOnItems(doubleHeight: Int) {
        val adapter = this.adapter ?: return
        val totalItems = adapter.itemCount
        val itemHeight = doubleHeight // Approximate height of one item in pixels
        val totalHeight = totalItems * itemHeight
        val layoutParams = this.layoutParams
        layoutParams.height = totalHeight
        this.layoutParams = layoutParams
    }

    fun getRelativeTime(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            val diff = System.currentTimeMillis() - (date?.time ?: 0)
            when {
                diff < TimeUnit.MINUTES.toMillis(1) -> "Just now"
                diff < TimeUnit.HOURS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toMinutes(diff)} min ago"
                diff < TimeUnit.DAYS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toHours(diff)} hours ago"
                diff < TimeUnit.DAYS.toMillis(30) -> "${TimeUnit.MILLISECONDS.toDays(diff)} days ago"
                diff < TimeUnit.DAYS.toMillis(365) -> "${TimeUnit.MILLISECONDS.toDays(diff) / 30} months ago"
                else -> "${TimeUnit.MILLISECONDS.toDays(diff) / 365} years ago"
            }
        } catch (e: Exception) {
            "Unknown time"
        }
    }

    fun getRelativeTime(timestamp: Long): String {
        return try {
            val currentTime = System.currentTimeMillis()
            val diff = currentTime - timestamp

            when {
                diff < TimeUnit.MINUTES.toMillis(1) -> "Just now"
                diff < TimeUnit.HOURS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toMinutes(diff)} min ago"
                diff < TimeUnit.DAYS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toHours(diff)} hours ago"
                diff < TimeUnit.DAYS.toMillis(30) -> "${TimeUnit.MILLISECONDS.toDays(diff)} days ago"
                diff < TimeUnit.DAYS.toMillis(365) -> "${TimeUnit.MILLISECONDS.toDays(diff) / 30} months ago"
                else -> "${TimeUnit.MILLISECONDS.toDays(diff) / 365} years ago"
            }
        } catch (e: Exception) {
            "Unknown time"
        }
    }

    fun fixDownloadLink(originalLink: String): String {
        // Replace the incorrect base URL with the correct one
        return originalLink.replace("https://www.bteb.gov.bd", "https://bteb.gov.bd")
    }

    fun getDividedSemNo(input: String, is1stChar: Boolean) : String{
        // Separate the number and text using a regular expression
        val regex = "(\\d+)(\\D+)".toRegex()
        val matchResult = regex.find(input)

        return if (is1stChar) matchResult?.groupValues?.get(1).toString() // Extracts "5"
        else matchResult?.groupValues?.get(2).toString() // Extracts "th"

    }
}