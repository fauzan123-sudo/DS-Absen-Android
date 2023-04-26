package com.infinity.dsmabsen.ui.fragment

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.CustomUbahPasswordBinding
import com.infinity.dsmabsen.databinding.FragmentBerandaBinding
import com.infinity.dsmabsen.helper.*
import com.infinity.dsmabsen.helper.Constans.IMAGE_URL
import com.infinity.dsmabsen.model.DataX
import com.infinity.dsmabsen.repository.NetworkResult
import com.infinity.dsmabsen.ui.activity.MainActivity
import com.infinity.dsmabsen.ui.viewModel.HomeViewModel
import com.infinity.dsmabsen.ui.viewModel.PasswordViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class BerandaFragment : BaseFragment<FragmentBerandaBinding>(FragmentBerandaBinding::inflate) {
    private val viewModel: HomeViewModel by viewModels()
    private val passwordViewModel: PasswordViewModel by viewModels()
    private val cacheManager = CacheManager()
    private lateinit var customAnalogClock: CustomAnalogClock
    private val handler = Handler()
    private lateinit var runnable: Runnable

    val savedUser = Paper.book().read<DataX>("user")

    @Inject
    lateinit var tokenManager: TokenManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideToolbar()
//        checkPassword(savedUser)
        val myActivities = activity as MainActivity
        myActivities.showMyBottomNav()

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

            materialCardView10.setOnClickListener {
                findNavController().navigate(R.id.action_berandaFragment_to_pengumumanFragment)
            }

            loadingInclude.loading.visibility = View.GONE
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
            binding.loadingInclude.loading.visibility = View.GONE
            when (it) {
                is NetworkResult.Success -> {
                    binding.loadingInclude.loading.visibility = View.GONE
                    binding.infoUser.isVisible = true
                    binding.materialCardView11.isVisible = true

                    binding.apply {
                        val dataHome = it.data!!.data
                        textView6.text = dataHome.nama
                        textView6.invalidate()
                        textView6.requestLayout()
                        textView7.text = dataHome.jabatan

                        Glide.with(requireContext())
                            .load(savedUser!!.image)
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
                    }


                    val status = it.data!!.status
                    if (status) {
                        Log.d("hi", "hi")
                    } else {
                        handleApiError(it.message)
                    }
                }
                is NetworkResult.Error -> {
                    binding.loadingInclude.loading.visibility = View.GONE
                    handleApiError(it.message)
                }

                is NetworkResult.Loading -> {
                    binding.loadingInclude.loading.visibility = View.VISIBLE
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
                    binding.loadingInclude.loading.visibility = View.GONE
                    binding.infoUser.isVisible = true
                    binding.materialCardView11.isVisible = true
                    binding.apply {
                        val response = it.data!!
                        tvCheckin.text = response.data.datang
                        tvCheckout.text = response.data.pulang
                        tvVisit.text = response.data.visit
                        tvBreak.text = response.data.istirahat

                    }
                }

                is NetworkResult.Loading -> {
                    binding.loadingInclude.loading.visibility = View.VISIBLE
                    binding.infoUser.isVisible = false
                    binding.materialCardView11.isVisible = false
                }

                is NetworkResult.Error -> {
                    binding.loadingInclude.loading.visibility = View.GONE
                    binding.infoUser.isVisible = true
                    binding.materialCardView11.isVisible = true
                    handleApiError(it.message)
                }
            }
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
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
            toolbar.toolbar.visibility = View.GONE
            scrollView.visibility = View.VISIBLE
            noInternetConnection.ivNoConnection.visibility = View.GONE
        }
    }

    override fun onConnectionLost() {
        super.onConnectionLost()
        binding.apply {
            toolbar.toolbar.visibility = View.GONE
            scrollView.visibility = View.VISIBLE
            noInternetConnection.ivNoConnection.visibility = View.VISIBLE
        }
    }
}