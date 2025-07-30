package com.rudhashi.btebhub.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rudhashi.btebhub.model.Favorite
import com.rudhashi.btebhub.utils.SmartData.getRelativeTime
import com.rudhashi.btebhub.databinding.ItemFavoriteBinding

class FavoriteAdapter(
    private val onItemClickListener: (Favorite) -> Unit
) :
    ListAdapter<Favorite, FavoriteAdapter.FavoriteViewHolder>(FavoriteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favorite = getItem(position)
        holder.bind(favorite, position)
    }

    inner class FavoriteViewHolder(private val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(favorite: Favorite, position: Int) {
            binding.rollText.text = favorite.rollNoOrRollComb
            //binding.instituteText.text = favorite.instituteName
            binding.nameText.text = favorite.name
            binding.timestampText.text = getRelativeTime(favorite.timestamp)

            // Set the item click listener
            binding.root.setOnClickListener {
                onItemClickListener(favorite)
            }

            binding.btnResult.text = if (favorite.isSingle) "View Result" else "View Group Result"

            val serialNo = position + 1
            val serial = serialNo.toString()
            binding.textViewSerial.text = serial
        }
    }

    class FavoriteDiffCallback : DiffUtil.ItemCallback<Favorite>() {
        override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return oldItem.rollNoOrRollComb == newItem.rollNoOrRollComb
        }

        override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return oldItem == newItem
        }
    }
}