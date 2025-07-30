package com.rudhashi.btebhub.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.rudhashi.btebhub.utils.ConverterHelper
import com.rudhashi.btebhub.utils.ConverterHelper.getSemToNo
import com.rudhashi.btebhub.utils.ConverterHelper.getTechToExam
import com.rudhashi.btebhub.R
import com.rudhashi.btebhub.databinding.FragmentGroupResultBinding

class GroupResultFragment : Fragment() {

    private lateinit var binding: FragmentGroupResultBinding
    private lateinit var navController: NavController
    private val technologyList = ConverterHelper.technologyList
    private val semesterList = listOf("1st Semester", "2nd Semester", "3rd Semester", "4th Semester", "5th Semester", "6th Semester", "7th Semester", "8th Semester")
    private val regulationList = listOf("2010", "2016", "2022")

    private var selectedTechnology: String? = null
    private var selectedSemester: String? = null
    private var selectedRegulation: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout and initialize binding
        binding = FragmentGroupResultBinding.inflate(inflater, container, false)
        navController = findNavController()

        setupDropdownMenus()

        binding.buttonViewResult.setOnClickListener {
            if (validateInputs()) {
                // Use selectedTechnology, selectedSemester, selectedRegulation for API call
                fetchResults()
            } else {
                Toast.makeText(requireContext(), "Please select all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        return binding.root
    }

    private fun setupDropdownMenus() {
        // Set up adapters for Material Dropdowns
        val technologyAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, technologyList)
        val semesterAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, semesterList)
        val regulationAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, regulationList)

        // Assign adapters to AutoCompleteTextViews
        binding.autoCompleteTechnology.setAdapter(technologyAdapter)
        binding.autoCompleteSemester.setAdapter(semesterAdapter)
        binding.autoCompleteRegulation.setAdapter(regulationAdapter)

        // Set up item selection listeners
        binding.autoCompleteTechnology.setOnItemClickListener { _, _, position, _ ->
            selectedTechnology = technologyList[position]
        }
        binding.autoCompleteSemester.setOnItemClickListener { _, _, position, _ ->
            selectedSemester = semesterList[position]
        }
        binding.autoCompleteRegulation.setOnItemClickListener { _, _, position, _ ->
            selectedRegulation = regulationList[position]
        }
    }

    private fun validateInputs(): Boolean {
        return selectedTechnology != null && selectedSemester != null && selectedRegulation != null
    }

    private fun fetchResults() {

        // Get data from spinners
        val exam = selectedTechnology
        val semesterPosition = selectedSemester
        val regulation = selectedRegulation

        // Get data from EditTexts
        val startRoll = binding.editTextStartRoll.text.toString()
        val endRoll = binding.editTextEndRoll.text.toString()

        // Create rollComb
        val rollComb = "$startRoll-$endRoll"

        binding.apply {
            // Open ResultActivity with the entered roll number
            @Suppress("KotlinConstantConditions")
            if (startRoll.length == 6 && endRoll.length == 6) {
                // Navigate to DetailsGroupResultFragment with arguments
                val action = GroupResultFragmentDirections.actionGroupResultFragmentToDetailsGroupResultFragment(
                    exam = getTechToExam(exam.toString()), // API format
                    semester = getSemToNo(semesterPosition.toString()),
                    regulation = regulation.toString(),
                    rollComb = rollComb
                )
                navController.navigate(action)
            }
            else if (startRoll.isEmpty() || endRoll.isEmpty()) {
                if (startRoll.isEmpty()) editTextStartRoll.error = "Please enter a start roll"
                if (endRoll.isEmpty()) editTextEndRoll.error = "Please enter a end roll"
                return
            }
            else if (startRoll.length != 6 || endRoll.length != 6) {
                 if (startRoll.length!= 6)  editTextStartRoll.error = "Start roll number must be 6 digits"
                 else if (endRoll.length!= 6)  editTextEndRoll.error = "End roll number must be 6 digits"
                return
            }
            else {
                editTextStartRoll.error = "Start roll number must be 6 digits"
                editTextEndRoll.error = "End roll number must be 6 digits"
                return
            }
        }

    }
}