package com.rudhashi.btebhub.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rudhashi.btebhub.utils.ConverterHelper
import com.rudhashi.btebhub.R
import com.rudhashi.btebhub.databinding.FragmentSingleResultBinding

class SingleResultFragment : Fragment() {

    private lateinit var binding: FragmentSingleResultBinding
    private val technologyList = ConverterHelper.technologyList
    private var selectedTechnology: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSingleResultBinding.inflate(inflater, container, false)

        setupDropdownMenus()

        binding.btnViewResult.setOnClickListener {
            if (selectedTechnology != null) fetchResults()
            else Toast.makeText(requireContext(), "Please select exam fields", Toast.LENGTH_SHORT).show()
        }

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        return binding.root
    }

    private fun setupDropdownMenus() {
        // Set up adapters for Material Dropdowns
        val technologyAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, technologyList)

        // Assign adapters to AutoCompleteTextViews
        binding.autoCompleteTechnology.setAdapter(technologyAdapter)

        // Set up item selection listeners
        binding.autoCompleteTechnology.setOnItemClickListener { _, _, position, _ ->
            selectedTechnology = technologyList[position]
        }
    }

    private fun fetchResults() {
        // Get data from spinners
        val exam = selectedTechnology
        Log.i("Exam", exam.toString())

        binding.apply {
            // Open ResultActivity with the entered roll number
            val rollNumber = etRoll.text.toString()
            if (rollNumber.length == 6){
                val action = SingleResultFragmentDirections.actionIndividualResultFragmentToDetailsSingleResultFragment(rollNumber)
                findNavController().navigate(action)
            }
            else if (rollNumber.isEmpty()) etRoll.error = "Please enter a roll number"
            else etRoll.error = "Roll number must be 6 digits"
        }
    }
}