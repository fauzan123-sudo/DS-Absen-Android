package com.infinity.dsmabsen.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.infinity.dsmabsen.R

abstract class BasesFragment<VB : ViewBinding>(
    private val bindingInflater: (inflater: LayoutInflater) -> VB
) : Fragment() {
    lateinit var bottomNavigationView: BottomNavigationView
    var _binding: VB? = null

//    protected lateinit var connectionLiveData: ConnectionLiveData
    val binding: VB
        //        get() = _binding as VB
        get() = _binding ?: throw IllegalStateException("ViewBinding is not initialized.")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater)
        if (_binding == null)
            throw IllegalArgumentException("Binding cannot be null")
//        toolbar = binding.root.findViewById(R.id.toolbarImage)
//        val icon = ContextCompat.getDrawable(
//            requireContext(),
//            R.drawable.ic_back
//        ) // ganti dengan icon navigasi Anda
//        DrawableCompat.setTint(
//            icon!!,
//            ContextCompat.getColor(requireContext(), R.color.black)
//        )
//        toolbar.navigationIcon = icon
        bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setupConnection()
    }

//    private fun setupConnection() {
//        connectionLiveData =
//            ConnectionLiveData(requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
//        connectionLiveData.observe(viewLifecycleOwner) { isConnected ->
//            if (isConnected) {
//                onConnectionAvailable()
//            } else {
//                onConnectionLost()
//            }
//        }
//    }

//    protected open fun onConnectionAvailable() {
//
//    }
//
//    protected open fun onConnectionLost() {
//
//    }


    fun showBottomNavigation() {
        Log.d("BN", "SHOW IN BASE")
        bottomNavigationView.visibility = View.GONE
    }

    fun hideBottomNavigation() {
        Log.d("BN", "HIDE IN BASE")
        bottomNavigationView.visibility = View.GONE
    }

}