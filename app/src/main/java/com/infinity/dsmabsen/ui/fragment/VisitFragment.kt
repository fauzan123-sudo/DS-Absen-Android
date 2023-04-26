package com.infinity.dsmabsen.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.adapter.VisitAdapter
import com.infinity.dsmabsen.databinding.FragmentVisitBinding
import com.infinity.dsmabsen.helper.handleApiError
import com.infinity.dsmabsen.model.DataX
import com.infinity.dsmabsen.repository.NetworkResult
import com.infinity.dsmabsen.ui.viewModel.UserProfileViewModel
import com.infinity.dsmabsen.ui.viewModel.VisitViewModel
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import io.paperdb.Paper

@AndroidEntryPoint
class VisitFragment : BasesFragment<FragmentVisitBinding>(FragmentVisitBinding::inflate) {

    private val viewModel: VisitViewModel by viewModels()
    private val userProfileViewModel: UserProfileViewModel by viewModels()
    private lateinit var adapter: VisitAdapter
    private lateinit var recyclerView: RecyclerView
    val savedUser = Paper.book().read<DataX>("user")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            btnAddVisit.setOnClickListener {
                findNavController().navigate(R.id.action_visitFragment2_to_penganjuanVisitFragment)
            }
            loadingInclude.loading.visibility = View.VISIBLE
            imgNoData.isVisible = false
//            val toolbar = toolbar
            toolbar.toolbarImage.title = "Visit"

            userProfileViewModel.profileUserRequest(savedUser!!.nip)
            userProfileViewModel.profileUserLivedata.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        loadingInclude.loading.visibility = View.GONE
                        binding.btnAddVisit.visibility = View.VISIBLE
                        val response = it.data!!
                        val status = response.status
                        val imageUser = response.data.foto
                        if (status) {
                            Glide.with(requireContext())
                                .load(imageUser)
                                .circleCrop()
                                .placeholder(R.drawable.user)
                                .into(toolbar.toolbarImageView)
                        }
                    }

                    is NetworkResult.Loading -> {
                        binding.apply {
                            binding.btnAddVisit.visibility = View.GONE
                            loadingInclude.loading.visibility = View.VISIBLE
                            recVisit.visibility = View.GONE
                        }
                    }

                    is NetworkResult.Error -> {
                        binding.apply {
                            binding.btnAddVisit.visibility = View.VISIBLE
                            loadingInclude.loading.visibility = View.GONE
                            recVisit.visibility = View.VISIBLE
                        }
                        handleApiError(it.message)
                    }
                }
            }

            viewModel.visitRequest(savedUser!!.nip)
            viewModel.visitLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        loadingInclude.loading.visibility = View.GONE
                        val response = it.data!!
                        val status = response.status
                        if (status) {
                            if (response.data.isEmpty()) {
                                recVisit.isVisible = false
                                imgNoData.isVisible = true
                            } else {
                                adapter = VisitAdapter(requireContext(), response.data) { visit ->
                                    val action =
                                        VisitFragmentDirections.actionVisitFragment2ToDetailVisitFragment(
                                            visit
                                        )
                                    findNavController().navigate(action)
                                }
                                recyclerView = recVisit
                                recyclerView.adapter = adapter
                                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                                recyclerView.setHasFixedSize(true)
//                                adapter.differ.submitList(response.data)
                                recVisit.isVisible = true
                                imgNoData.isVisible = false
                            }
                        }
                    }

                    is NetworkResult.Loading -> {
                        binding.apply {
                            loadingInclude.loading.visibility = View.VISIBLE
                            recVisit.visibility = View.GONE
                        }
                    }

                    is NetworkResult.Error -> {
                        binding.apply {
                            loadingInclude.loading.visibility = View.GONE
                            recVisit.visibility = View.VISIBLE
                        }
                        handleApiError(it.message)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


