package com.rudhashi.btebhub.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rudhashi.btebhub.adapter.ResultAdapter
import com.rudhashi.btebhub.manager.DialogManager.showNameInputDialog
import com.rudhashi.btebhub.manager.DialogManager.updateFavoriteButton
import com.rudhashi.btebhub.manager.FavoriteManager
import com.rudhashi.btebhub.utils.RetrofitInstance
import com.rudhashi.btebhub.R
import com.rudhashi.btebhub.databinding.FragmentDetailsSingleResultBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class DetailsSingleResultFragment : Fragment() {

    private var _binding: FragmentDetailsSingleResultBinding? = null
    private val binding get() = _binding!!
    private lateinit var resultAdapter: ResultAdapter
    private lateinit var favoriteManager: FavoriteManager

    private var rollNumber: String = ""
    private var instituteName: String = "" // Initialize to an empty string

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsSingleResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteManager = FavoriteManager(requireContext())

        binding.apply {
            image.visibility = View.VISIBLE
            containerLay.visibility = View.INVISIBLE
        }

        // Set up RecyclerView
        resultAdapter = ResultAdapter()
        binding.resultRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.resultRecyclerView.adapter = resultAdapter
        setRecyclerViewHeightBasedOnItems(binding.resultRecyclerView)

        // Get roll number from the Arguments
        rollNumber = arguments?.let { DetailsSingleResultFragmentArgs.fromBundle(it).roll }.toString()
        binding.tvHeading.text = "Results for Roll $rollNumber"

        // Check if the roll number is already in favorites
        updateFavoriteButton(requireActivity(), rollNumber, binding.btnFavorite)

        // Fetch and display results
        fetchResult(rollNumber)

        // Handle button click
        binding.btnFavorite.setOnClickListener {
            if (favoriteManager.isFavorite(rollNumber)) {
                // Remove from favorites
                favoriteManager.removeFavorite(rollNumber)
                Toast.makeText(requireActivity(), "Removed from favorites", Toast.LENGTH_SHORT).show()
            } else {
                // Show AlertDialog to enter name
                showNameInputDialog(requireActivity(), rollNumber, instituteName, "", "", true , binding.btnFavorite)
            }
            // Update the button label after toggling

            updateFavoriteButton(requireActivity(), rollNumber, binding.btnFavorite)
        }

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
    }

    // === === === === === === === === === === === === === === === === === === === === === === === === ===

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchResult(rollNumber: String) {
        val apiService = RetrofitInstance.api

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Fetch results from the API
                val resultList = apiService.getResults(rollNumber, "diploma in engineering")

                withContext(Dispatchers.Main) {
                    if (resultList.isNotEmpty()) {
                        // Sort the results by date
                        val sortedResults = resultList.sortedByDescending { result ->
                            try {
                                // Parse the date field into a Date object
                                SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(result.Date)
                            } catch (e: Exception) {
                                null // Handle any parsing errors gracefully
                            }
                        }

                        // Display top info from the first result
                        val firstResult = sortedResults[0]
                        binding.apply {
                            val tech = firstResult.technology.split(" ")
                                .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }

                            technologyText.text = getString(R.string.technology_regulation, tech, firstResult.regulation)
                            instituteName = firstResult.institute
                            instituteText.text = instituteName

                            // Submit the sorted list to the adapter
                            resultAdapter.submitList(sortedResults)
                            resultAdapter.notifyDataSetChanged()
                        }

                        // Submit the sorted list to the adapter
                        resultAdapter.submitList(sortedResults)
                        resultAdapter.notifyDataSetChanged()

                        binding.apply {
                            image.visibility = View.GONE
                            containerLay.visibility = View.VISIBLE
                        }
                    } else {
                        // Handle no results
                        binding.apply {
                            technologyText.text = "No result found!"
                            instituteText.visibility = View.GONE
                        }
                        binding.apply {
                            image.visibility = View.GONE
                            containerLay.visibility = View.VISIBLE
                        }
                    }
                }
            } catch (e: Exception) {
                // Handle API call error
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    binding.apply {
                        technologyText.text = "No result found!"
                        instituteText.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setRecyclerViewHeightBasedOnItems(recyclerView: RecyclerView) {
        val adapter = recyclerView.adapter ?: return

        // Get total item count
        val itemCount = adapter.itemCount

        if (itemCount == 0) return

        // Initialize total height
        var totalHeight = 0

        for (i in 0 until itemCount) {
            val viewHolder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i))
            val itemView = viewHolder.itemView

            // Measure item view height
            itemView.measure(
                View.MeasureSpec.makeMeasureSpec(recyclerView.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.UNSPECIFIED
            )
            totalHeight += itemView.measuredHeight
        }

        // Set RecyclerView height
        val params = recyclerView.layoutParams
        params.height = totalHeight + (recyclerView.itemDecorationCount * 10) // Add padding for decorations
        recyclerView.layoutParams = params
        recyclerView.requestLayout()
    }
}