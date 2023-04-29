package com.infinity.dsmabsen.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.FragmentScanBinding

class ScanFragment : BaseFragment<FragmentScanBinding>(FragmentScanBinding::inflate){
    private lateinit var codeScanner: CodeScanner

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar("Scan Barcode")
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                else -> false
            }
        }
        setHasOptionsMenu(true)
        val scannerView = binding.scannerView
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                val data = it.text
                Log.d("scan", data)
                val fragment = PenganjuanVisitFragment()
                val bundle = Bundle()
                bundle.putString("data", data)
                fragment.arguments = bundle
                val fragmentTransaction = parentFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragmentContainerView2, fragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()

                val action =
                    ScanFragmentDirections.actionScanFragmentToPenganjuanVisitFragment(data)
                findNavController().navigate(action)
//                Toast.makeText(activity, it.text, Toast.LENGTH_LONG).show()
            }
        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        val menuSave = menu.findItem(R.id.save)
        val menuPlus = menu.findItem(R.id.add)
        val menuLogout = menu.findItem(R.id.logout)
        val menuScan = menu.findItem(R.id.scan)

        menuScan.isVisible = false
        menuLogout.isVisible = false
        menuSave?.isVisible = false
        menuPlus?.isVisible = false
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}