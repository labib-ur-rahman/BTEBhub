package com.rudhashi.btebhub.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rudhashi.btebhub.adapter.FavoriteAdapter
import com.rudhashi.btebhub.manager.FavoriteManager
import com.rudhashi.btebhub.databinding.FragmentFavouriteBinding

class FavouriteFragment : Fragment() {

    private lateinit var binding: FragmentFavouriteBinding
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var favoriteManager: FavoriteManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)

        favoriteManager = FavoriteManager(requireActivity())

        // Initialize RecyclerView
        binding.rvFavorites.layoutManager = LinearLayoutManager(requireContext())

        loadFavorites()

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        return binding.root
    }

    // ----------------------------------------------------------------
    private fun loadFavorites() {
        // Initialize the adapter with the click listener
        favoriteAdapter = FavoriteAdapter { favorite ->
            if (favorite.isSingle){
                val action = FavouriteFragmentDirections.actionFavouriteFragmentToDetailsSingleResultFragment(favorite.rollNoOrRollComb)
                findNavController().navigate(action)
            } else {
                val action = FavouriteFragmentDirections.actionFavouriteFragmentToDetailsGroupResultFragment(
                    exam = "DIPLOMA+IN+ENGINEERING", // API format
                    semester = favorite.semester,
                    regulation = favorite.regulation,
                    rollComb = favorite.rollNoOrRollComb
                )
                findNavController().navigate(action)
            }

        }
        binding.rvFavorites.adapter = favoriteAdapter
        // Get sorted list of favorites and set it to the adapter
        val sortedFavorites = favoriteManager.getFavorites()
        favoriteAdapter.submitList(sortedFavorites)
    }

    override fun onResume() {
        super.onResume()
        // Refresh the RecyclerView when returning to FavoriteFragment
        loadFavorites()
    }

}