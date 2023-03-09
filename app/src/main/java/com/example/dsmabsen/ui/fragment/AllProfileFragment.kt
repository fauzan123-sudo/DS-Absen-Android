package com.example.dsmabsen.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.dsmabsen.R
import com.example.dsmabsen.databinding.FragmentAllProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllProfileFragment :BaseFragment<FragmentAllProfileBinding>(FragmentAllProfileBinding::inflate){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            materialCardView1.setOnClickListener {
                findNavController().navigate(R.id.action_allProfileFragment_to_personalDataFragment)
            }
        }

    }
}