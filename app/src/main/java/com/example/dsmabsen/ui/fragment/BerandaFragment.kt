package com.example.dsmabsen.ui.fragment

import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.dsmabsen.R
import com.example.dsmabsen.databinding.FragmentBerandaBinding
import com.example.dsmabsen.helper.*
import com.example.dsmabsen.helper.Constans.IMAGE_URL
import com.example.dsmabsen.model.DataX
import com.example.dsmabsen.repository.NetworkResult
import com.example.dsmabsen.ui.viewModel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class BerandaFragment : BaseFragment<FragmentBerandaBinding>(FragmentBerandaBinding::inflate) {
    private val viewModel: HomeViewModel by viewModels()
    private val cacheManager = CacheManager()
    private lateinit var customAnalogClock: CustomAnalogClock
    private val handler = Handler()
    private lateinit var runnable: Runnable


    @Inject
    lateinit var tokenManager: TokenManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideToolbar()


        val data = cacheManager.getPass()
        val savedUser = Paper.book().read<DataX>("user")

        with(binding) {

            materialCardView8.setOnClickListener {
                findNavController().navigate(R.id.action_berandaFragment_to_sallaryFragment)
            }

            materialCardView5.setOnClickListener {
                findNavController().navigate(R.id.action_berandaFragment_to_menuPerizinanFragment)
            }

            materialCardView9.setOnClickListener {
                findNavController().navigate(R.id.action_berandaFragment_to_reimbursementFragment)
            }

            materialCardView6.setOnClickListener {
                findNavController().navigate(R.id.action_berandaFragment_to_lemburFragment)
            }

            materialCardView7.setOnClickListener {
                findNavController().navigate(R.id.action_berandaFragment_to_shiftFragment)
            }

            constraint.setOnClickListener {
                findNavController().navigate(R.id.action_berandaFragment_to_allMenuFragment)
            }

            loadingInclude.loading.isVisible = false

            val customAnalogClock = binding.customAnalogClock
            // Set waktu pada custom analog clock
            val calendar = Calendar.getInstance()
            customAnalogClock.setTime(
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND)
            )

            // Update waktu setiap detik
            val timer = Timer()
            timer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    activity?.runOnUiThread {
                        val calendar = Calendar.getInstance()
                        customAnalogClock.setTime(
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            calendar.get(Calendar.SECOND)
                        )
                    }
                }
            }, 0, 1000)

            runnable = object : Runnable {
                override fun run() {
                    val timestamp = System.currentTimeMillis()

                    // Create a SimpleDateFormat object with the system timezone
                    val sdfJam = SimpleDateFormat("HH", Locale.getDefault())
                    val sdfMenit = SimpleDateFormat("mm", Locale.getDefault())
                    val sdfDetik = SimpleDateFormat("ss", Locale.getDefault())
                    sdfJam.timeZone = TimeZone.getDefault()
                    sdfMenit.timeZone = TimeZone.getDefault()
                    sdfDetik.timeZone = TimeZone.getDefault()
                    handler.postDelayed(this, 1000)
                }
            }

            materialCardView9.setOnClickListener {
                findNavController().navigate(R.id.action_berandaFragment_to_reimbursementFragment)
            }
        }
        viewModel.homeRequest(savedUser!!.nip)
        viewModel.homeLiveData.observe(viewLifecycleOwner) {
            binding.loadingInclude.loading.isVisible = true
            when (it) {
                is NetworkResult.Success -> {
                    binding.loadingInclude.loading.isVisible = false
                    binding.infoUser.isVisible = true
                    binding.materialCardView11.isVisible = true

                    binding.apply {
                        val dataHome = it.data!!.data
                        textView6.text = dataHome.nama
                        textView6.invalidate()
                        textView6.requestLayout()
                        textView7.text = dataHome.jabatan

                        Glide.with(requireContext())
                            .load(IMAGE_URL + "/" + dataHome.foto)
                            .into(imageView3)

                        textView8.text = dataHome.nama_shift
                        textView9.text = dataHome.jam_shift
                        if (Build.VERSION.SDK_INT < 26) {
                            day.text = HelperApiUnder().getDayApi23()
                            textView10.text = HelperApiUnder().getDate()
                            textView11.text = HelperApiUnder().getMY()
                        } else {
                            day.text = Helper().getDay()
                            textView10.text = Helper().getDate()
                            textView11.text = Helper().getMY()
                        }

                        imageView3.setOnClickListener {
                            findNavController().navigate(R.id.action_berandaFragment_to_profileFragment)
                        }

                    }


                    val status = it.data!!.status
                    if (status) {
                        Log.d("hi", "hi")
                    } else {
                        handleApiError(it.message)
                    }
                }
                is NetworkResult.Error -> {
                    binding.loadingInclude.loading.isVisible = false
                    handleApiError(it.message)
                }

                is NetworkResult.Loading -> {
                    binding.loadingInclude.loading.isVisible = true
                    binding.infoUser.isVisible = false
                    binding.materialCardView11.isVisible = false
                }

                else -> Log.d("else", "else")

            }
        }

        viewModel.getAbsenRequest(savedUser!!.nip)
        viewModel.getAbsenLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    binding.apply {
                        val response = it.data!!
                        tvCheckin.text = response.data.datang
                        tvCheckout.text = response.data.pulang
                    }
                }

                is NetworkResult.Loading -> {
                    Log.d("TAG", "onViewCreated: ")
                }

                is NetworkResult.Error -> {
                    handleApiError(it.message)
                }
            }
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Navigasi kembali ke halaman sebelumnya jika tidak berada di halaman awal (DefaultFragment)
                val navController = findNavController()
                if (navController.currentDestination?.id == R.id.berandaFragment) {
//                    activity?.finish()
                    showExitConfirmationDialog()
                } else {
                    navController.navigateUp()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }


    fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Konfirmasi")
        builder.setMessage("Apakah Anda yakin ingin keluar dari aplikasi?")
        builder.setPositiveButton("Ya") { _, _ ->
            activity?.finish()
        }

        builder.setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()

    }

    override fun onConnectionAvailable() {
        super.onConnectionAvailable()
        binding.apply {
            toolbar.toolbar.visibility = View.VISIBLE
            scrollView.visibility = View.VISIBLE
            noInternetConnection.ivNoConnection.visibility = View.GONE
            Toast.makeText(requireContext(), "terkonekasi", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onConnectionLost() {
        super.onConnectionLost()
        binding.apply {
            toolbar.toolbar.visibility = View.GONE
            scrollView.visibility = View.GONE
            noInternetConnection.ivNoConnection.visibility = View.VISIBLE
        }
        Toast.makeText(requireContext(), "tidak terkonekasi", Toast.LENGTH_SHORT).show()
    }
}