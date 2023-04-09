package com.infinity.dsmabsen.ui.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.viewbinding.ViewBinding
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.helper.ConnectionLiveData
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.infinity.dsmabsen.ui.activity.MainActivity

abstract class BaseFragment<VB : ViewBinding>(
    private val bindingInflater: (inflater: LayoutInflater) -> VB
) : Fragment() {
    lateinit var bottomNavigationView: BottomNavigationView
    protected lateinit var toolbar: Toolbar
    var _binding: VB? = null

    protected lateinit var connectionLiveData: ConnectionLiveData
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
        toolbar = binding.root.findViewById(R.id.toolbar)
        bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupConnection()
    }

    private fun setupConnection() {
        connectionLiveData =
            ConnectionLiveData(requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
        connectionLiveData.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                onConnectionAvailable()
            } else {
                onConnectionLost()
            }
        }
    }

    protected open fun onConnectionAvailable() {

    }

    protected open fun onConnectionLost() {

    }

    protected open fun setupToolbar(title: String) {

        val customToolbar = layoutInflater.inflate(R.layout.toolbar_layout, null) as Toolbar
        val navController = findNavController()
        val config = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, config)
        customToolbar.setupWithNavController(navController, config)

        // Ubah font pada TextView judul Toolbar
        val titleTextView = customToolbar.findViewById<TextView>(R.id.toolbar_title)
        titleTextView.text = title
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = title
        // Hide title
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        toolbar.setPaddingRelative(0, 0, 0, 0)
        toolbar.titleMarginStart = -10




        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
//                R.id.add -> {
//                    // Handle menu item 1 click
//                    true
//                }
//                R.id.save -> {
//                    // Handle menu item 2 click
//                    true
//                }
                else -> false
            }
        }
    }

    fun hideToolbar() {
        toolbar.visibility = View.GONE
    }

    fun showBottomNavigation() {
        bottomNavigationView.visibility = View.VISIBLE
    }

    fun hideBottomNavigation() {
        bottomNavigationView.visibility = View.GONE
    }

}