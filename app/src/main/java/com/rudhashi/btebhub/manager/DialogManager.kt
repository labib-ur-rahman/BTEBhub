package com.rudhashi.btebhub.manager

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.FragmentActivity
import com.rudhashi.btebhub.model.Favorite
import com.rudhashi.btebhub.R
import com.rudhashi.btebhub.activity.DashboardActivity
import com.rudhashi.btebhub.databinding.DialogConfirmDownloadBinding
import com.rudhashi.btebhub.databinding.DialogInputNameBinding

object DialogManager {

    @SuppressLint("InflateParams")
    fun showNameInputDialog(
        context: Context,
        rollNoOrRollComb: String,
        institute: String,
        semester: String,
        regulation: String,
        isSingle: Boolean,
        btnFavorite: AppCompatButton
    ) {
        val builder = AlertDialog.Builder(context).create()
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_input_name, null)
        val dialogBinding = DialogInputNameBinding.bind(dialogView)
        builder.setView(dialogBinding.root)
        builder.setCanceledOnTouchOutside(false)
        builder.show()

        dialogBinding.apply {
            btnSave.setOnClickListener {
                val name = etName.text.toString()
                if (name.isNotEmpty()) {
                    // Add to favorites with timestamp
                    val timestamp = System.currentTimeMillis() // Get current timestamp
                    FavoriteManager(context).addFavorite(Favorite(name, rollNoOrRollComb, institute, semester, regulation, timestamp, isSingle))
                    Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Name cannot be empty", Toast.LENGTH_SHORT).show()
                }
                updateFavoriteButton(context, rollNoOrRollComb, btnFavorite)
                builder.dismiss()
            }
            btnCancel.setOnClickListener { builder.dismiss() }
        }

        if (builder.window != null) builder.window!!.setBackgroundDrawable(ColorDrawable(0))
        builder.show()
    }

    fun showConfirmDownloadDialog(
        context: Context,
        activity: FragmentActivity,
        url: String
    ) {
        val builder = AlertDialog.Builder(context).create()
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_confirm_download, null)
        val dialogBinding = DialogConfirmDownloadBinding.bind(dialogView)
        builder.setView(dialogBinding.root)
        builder.setCanceledOnTouchOutside(false)
        builder.show()

        dialogBinding.apply {
            btnDownload.setOnClickListener {
                (activity as? DashboardActivity)?.startDownloading(url)
                builder.dismiss()
            }
            btnCancel.setOnClickListener { builder.dismiss() }
        }

        if (builder.window != null) builder.window!!.setBackgroundDrawable(ColorDrawable(0))
        builder.show()
    }

    // === === === === === === === === === === === === === === === === === === === === === === === === ===
    // ============ Additional ============
    fun updateFavoriteButton(context: Context, rollNumber: String, btnFavorite: AppCompatButton) {
        if (FavoriteManager(context).isFavorite(rollNumber)) {
            btnFavorite.text = context.getString(R.string.remove_from_favorite)
            btnFavorite.setBackgroundResource(R.drawable.fav_remove)
        } else {
            btnFavorite.text = context.getString(R.string.add_to_favorite)
            btnFavorite.setBackgroundResource(R.drawable.fav_add)
        }
    }

}