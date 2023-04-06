package com.infinity.dsmabsen.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.FragmentScanBinding
import com.infinity.dsmabsen.model.Code

class ScanFragment : Fragment() {
    private lateinit var codeScanner: CodeScanner
    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}