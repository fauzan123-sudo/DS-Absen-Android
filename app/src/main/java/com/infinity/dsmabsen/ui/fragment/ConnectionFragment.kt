package com.infinity.dsmabsen.ui.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.infinity.dsmabsen.helper.ConnectionLiveData

class ConnectionFragment (){

}
//abstract class ConnectionFragment<VB : ViewBinding>(
//    private val bindingInflater: (inflater: LayoutInflater) -> VB
//) : BaseFragment<VB>(bindingInflater) {
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        connectionLiveData =
//            ConnectionLiveData(requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
//        connectionLiveData.observe(viewLifecycleOwner) { isConnected ->
////            if (isConnected) {
////                onConnectionAvailable()
////            } else {
////                onConnectionLost()
////            }
////        }
//        }
//
////    protected open fun onConnectionAvailable() {}
////
////    protected open fun onConnectionLost() {}
//    }
//}
