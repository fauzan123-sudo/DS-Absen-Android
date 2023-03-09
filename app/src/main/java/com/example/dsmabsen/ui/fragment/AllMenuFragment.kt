package com.example.dsmabsen.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dsmabsen.R
import com.example.dsmabsen.databinding.FragmentAllMenuBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllMenuFragment :BaseFragment<FragmentAllMenuBinding>(FragmentAllMenuBinding::inflate){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}