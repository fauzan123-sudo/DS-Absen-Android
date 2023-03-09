package com.example.dsmabsen.ui.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
import javax.inject.Inject

@AndroidEntryPoint
class BerandaFragment : BaseFragment<FragmentBerandaBinding>(FragmentBerandaBinding::inflate) {
    private val viewModel: HomeViewModel by viewModels()
    private val cacheManager = CacheManager()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = cacheManager.getPass()
        val savedUser = Paper.book().read<DataX>("user")
//        val token = tokenManager.getToken()

        with(binding) {
            constraint.setOnClickListener {
                findNavController().navigate(R.id.action_berandaFragment_to_allMenuFragment)
            }

            materialCardView9.setOnClickListener {
                findNavController().navigate(R.id.action_berandaFragment_to_reimbursementFragment)
            }
        }
        viewModel.homeRequest(savedUser!!.nip)
        viewModel.homeLiveData.observe(viewLifecycleOwner) {
            binding.loading.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    binding.homeVisible.isVisible = true
//                        binding.shimmerViewContainer.stopShimmer()
//                        binding.shimmerViewContainer.visibility = View.GONE

                    binding.apply {
                        val dataHome = it.data!!.data
                        textView6.text = dataHome.nama
                        textView6.invalidate()
                        textView6.requestLayout()
                        textView7.text = dataHome.jabatan
                        Glide.with(requireContext())
                            .load(IMAGE_URL + dataHome.foto)
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
//                            Toast.makeText(requireContext(), "Hi..", Toast.LENGTH_LONG).show()
                        Log.d("hi", "hi")
                    } else {
                        handleApiError(it.message)
                    }
                }
                is NetworkResult.Error -> {
//                        Toast.makeText(requireContext(), "tres", Toast.LENGTH_SHORT).show()
//                        binding.shimmerViewContainer.stopShimmer()
                    handleApiError(it.message)
                }

                is NetworkResult.Loading -> {
                    binding.loading.isVisible = true
                    binding.homeVisible.isVisible = false
//                        binding.shimmerViewContainer.startShimmer()
//                        binding.shimmerViewContainer.isVisible = true
                }

                else -> Log.d("else", "else")

            }
        }
    }
}