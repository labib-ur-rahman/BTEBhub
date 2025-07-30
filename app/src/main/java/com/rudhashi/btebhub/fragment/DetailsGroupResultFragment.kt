package com.rudhashi.btebhub.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.rudhashi.btebhub.adapter.ResultsAdapter
import com.rudhashi.btebhub.api.ResultsApi
import com.rudhashi.btebhub.manager.DialogManager.showNameInputDialog
import com.rudhashi.btebhub.manager.DialogManager.updateFavoriteButton
import com.rudhashi.btebhub.manager.FavoriteManager
import com.rudhashi.btebhub.model.GroupResultResponse
import com.rudhashi.btebhub.utils.ConverterHelper.formatDate
import com.rudhashi.btebhub.utils.ConverterHelper.getExamToTech
import com.rudhashi.btebhub.utils.ConverterHelper.getFormattedSemester
import com.rudhashi.btebhub.R
import com.rudhashi.btebhub.databinding.FragmentDetailsGroupResultBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailsGroupResultFragment : Fragment() {

    private lateinit var binding: FragmentDetailsGroupResultBinding
    private lateinit var adapter: ResultsAdapter // RecyclerView adapter
    private lateinit var favoriteManager: FavoriteManager

    private lateinit var exam: String
    private lateinit var semester: String
    private lateinit var regulation: String
    private lateinit var rollComb: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout and initialize binding
        binding = FragmentDetailsGroupResultBinding.inflate(inflater, container, false)
        favoriteManager = FavoriteManager(requireContext())

        binding.apply {
            image.visibility = View.VISIBLE
            containerLay.visibility = View.INVISIBLE
        }

        // Get arguments passed from GroupResultFragment
        try {
            arguments?.let {
                exam = it.getString("exam").toString()
                semester = it.getString("semester").toString()
                regulation = it.getString("regulation").toString()
                rollComb = it.getString("rollComb").toString()
            }
        } catch (e: Exception){
            exam = ""
            semester = ""
            regulation = ""
            rollComb = ""
        }

        // Check if the roll number is already in favorites
        updateFavoriteButton(requireActivity(), rollComb, binding.btnFavorite)

        // Initialize the adapter with an empty list and set it up
        adapter = ResultsAdapter(emptyList()) { roll ->
            val action = DetailsGroupResultFragmentDirections
                .actionDetailsGroupResultFragmentToDetailsSingleResultFragment(roll)
            findNavController().navigate(action)
        }

        binding.tvHeader.text = "Result for ${getFormattedSemester(semester)}"
        binding.recyclerViewResults.adapter = adapter
        binding.recyclerViewResults.layoutManager = GridLayoutManager(requireContext(), 2)

        // Hit the API
        fetchResults()

        // Handle button click
        binding.btnFavorite.setOnClickListener {
            if (favoriteManager.isFavorite(rollComb)) {
                // Remove from favorites
                favoriteManager.removeFavorite(rollComb)
                Toast.makeText(requireActivity(), "Removed from favorites", Toast.LENGTH_SHORT).show()
            } else {
                // Show AlertDialog to enter name
                showNameInputDialog(requireActivity(), rollComb, "", semester, regulation,false , binding.btnFavorite)
            }
            // Update the button label after toggling

            updateFavoriteButton(requireActivity(), rollComb, binding.btnFavorite)
        }

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        return binding.root
    }

    private fun fetchResults() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://btebresultszone.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ResultsApi::class.java)
        val call = api.getGroupResults(
            semester = semester.toInt(),
            rollComb = rollComb,
            exam = exam,
            regulation = regulation
        )

        call.enqueue(object : Callback<GroupResultResponse> {
            override fun onResponse(
                call: Call<GroupResultResponse>,
                response: Response<GroupResultResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        updateUI(data)
                    }
                    binding.apply {
                        image.visibility = View.GONE
                        containerLay.visibility = View.VISIBLE
                    }
                } else {
                    // Handle no results
                    binding.apply {
                        textViewExam.text = "No result found!"
                        textViewQueryRolls.visibility = View.GONE
                        textViewDate.visibility = View.GONE
                        textViewItemTotalCount.visibility = View.GONE
                    }
                    binding.apply {
                        image.visibility = View.GONE
                        containerLay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onFailure(call: Call<GroupResultResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUI(data: GroupResultResponse) {
        // Update TextViews
        val tech = getExamToTech(exam).split(" ")
            .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
        binding.textViewExam.text = getString(R.string.technology_regulation, tech, data.regulation.toString())
        binding.textViewQueryRolls.text = "Roll No. ${data.query_rolls.split("-").first()} to ${data.query_rolls.split("-").last()}"
        binding.textViewDate.text = "Publish on ${formatDate(data.results.first().result.date)}"
        binding.textViewItemTotalCount.text = "Total Result Found ${data.results.size}"

        // Update RecyclerView
        adapter.updateResults(data.results)
    }

}