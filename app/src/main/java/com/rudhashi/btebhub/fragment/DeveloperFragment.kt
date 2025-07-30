package com.rudhashi.btebhub.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.rudhashi.btebhub.databinding.FragmentDeveloperBinding

class DeveloperFragment : Fragment() {

    private lateinit var binding: FragmentDeveloperBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDeveloperBinding.inflate(inflater, container, false)

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        return binding.root
    }

}