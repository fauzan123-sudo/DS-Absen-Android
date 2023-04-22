package com.infinity.dsmabsen.ui.fragment

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.Manifest
import android.content.Context
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.infinity.dsmabsen.R
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.infinity.dsmabsen.adapter.SpinnerAdapter
import com.infinity.dsmabsen.databinding.FragmentFormPerizinanBinding
import com.infinity.dsmabsen.helper.handleApiError
import com.infinity.dsmabsen.model.DataX
import com.infinity.dsmabsen.model.DataXXXXXXXXXXXXX
import com.infinity.dsmabsen.repository.NetworkResult
import com.infinity.dsmabsen.ui.viewModel.PerizinanViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.infinity.dsmabsen.helper.AlertDialogHelper
import com.infinity.dsmabsen.ui.activity.MainActivity
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class FormPerizinanFragment :
    BaseFragment<FragmentFormPerizinanBinding>(FragmentFormPerizinanBinding::inflate) {
    private val viewModel: PerizinanViewModel by viewModels()
    val savedUser = Paper.book().read<DataX>("user")
    private var kode_perizinan: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
//        registerResult()
        hideBottomNavigation()
        val myActivities = activity as MainActivity
        myActivities.hideMyBottomNav()
        setupToolbar("Ajukan Perizinan")

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.save -> {
                    savePerizinan(savedUser)

                    true
                }

                else -> false
            }
        }

        with(binding) {

            pickImage.setOnClickListener {
                uploadFiles()
            }
            etTanggalMulai.setOnClickListener {
                getCalendarStart()
            }

            etTanggalSelesai.setOnClickListener {
                getCalendarEnd()
            }
            viewModel.requestgetSpinner()
            viewModel.getSpinnerLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        val data = it.data!!
                        val status = data.status
                        binding.apply {
                            loadingInclude.loading.visibility = View.GONE
                            scrollView.visibility = View.VISIBLE
                        }
                        if (status) {
                            val list = data.data.toMutableList()
                            val defaultItem = DataXXXXXXXXXXXXX(
                                "",
                                "pilih jenis perizinan",
                                0
                            )

                            list += listOf(defaultItem)

                            val spinnerAdapter = SpinnerAdapter(requireContext(), list)
                            spinnerJenisIzin.adapter = spinnerAdapter
                            spinnerJenisIzin.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long
                                    ) {
                                        val clickedItem: DataXXXXXXXXXXXXX =
                                            parent?.getItemAtPosition(position) as DataXXXXXXXXXXXXX
                                        val idSpinner = clickedItem.kode_cuti
                                        kode_perizinan = idSpinner
                                        Log.d("idSpinner", idSpinner)
                                    }

                                    override fun onNothingSelected(p0: AdapterView<*>?) {
                                        Log.d("nothing", "No")
                                    }

                                }
                        } else {
                        }
                    }

                    is NetworkResult.Loading -> {
                        binding.apply {
                            loadingInclude.loading.visibility = View.VISIBLE
                            scrollView.visibility = View.GONE
                        }
                    }

                    is NetworkResult.Error -> {
                        binding.apply {
                            loadingInclude.loading.visibility = View.GONE
                            scrollView.visibility = View.VISIBLE
                        }
                        handleApiError(it.message)
                    }

                    else -> ""
                }
            }

        }
        toolbar.setOnMenuItemClickListener { menuItem ->
            var i = 0
            when (menuItem.itemId) {
                R.id.save -> {

                    if (binding.etTanggalMulai.text.isEmpty()) {
                        binding.etTanggalMulai.error = "Harap isi bidang ini!!"
                        binding.etTanggalMulai.requestFocus()
                        i++

                    }
                    if (binding.etTanggalSelesai.text.isEmpty()) {
                        binding.etTanggalSelesai.error = "Harap isi bidang ini!!"
                        binding.etTanggalSelesai.requestFocus()
                        i++

                    }
                    if (binding.etKeterangan.text.isEmpty()) {
                        binding.etKeterangan.error = "Harap isi bidang ini!!"
                        binding.etKeterangan.requestFocus()
                        i++
                    }
                    if (i == 0) {
                        savePerizinan(savedUser)
                    }
                    true
                }

                else -> false
            }
        }
    }

    private fun uploadFiles() {
        val pictureDialog = AlertDialog.Builder(requireContext())
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Take Photo With Camera", "Select Photo From Gallery")
        pictureDialog.setItems(pictureDialogItems) { _, which ->
            when (which) {
                0 -> takePictureFromCamera()
                1 -> choosePictureFromGallery()
            }
        }
        pictureDialog.show()
    }

    private fun takePictureFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        putPhoto.launch(intent)
    }

    private val putPhoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                val bitmap = it?.data?.extras?.get("data") as Bitmap
                binding.uploadFile.setImageBitmap(bitmap)
                binding.uploadFile.visibility = View.VISIBLE
            } else if (it == null) {
                binding.uploadFile.visibility = View.GONE
            } else {
                Log.d("TAG", "Task Cancelled")
//                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    private fun choosePictureFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        putImage.launch(intent)
    }

    private val putImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                try {
                    val imageUri = it?.data?.data
                    binding.uploadFile.setImageURI(imageUri)
                    binding.uploadFile.visibility = View.VISIBLE
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Gagal memuat gambar", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        val menuSave = menu.findItem(R.id.save)
        val menuPlus = menu.findItem(R.id.add)
        val menuLogout = menu.findItem(R.id.logout)
        val menuScan = menu.findItem(R.id.scan)

        menuScan?.isVisible = false
        menuLogout.isVisible = false
        menuSave?.isVisible = true // menyembunyikan menu tertentu
        menuPlus?.isVisible = false // menyembunyikan menu tertentu

    }

    private fun getCalendarEnd() {
        val datePicker = MaterialDatePicker.Builder
            .datePicker()
            .setTitleText("")
            .build()
        datePicker.show(childFragmentManager, "DatePicker")

        datePicker.addOnPositiveButtonClickListener {
            val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
            val date = dateFormatter.format(Date(it))
            binding.etTanggalSelesai.text = date

        }

        datePicker.addOnNegativeButtonClickListener {
            Log.d("TAG", "getCalendarEnd: negetive button")
        }

        datePicker.addOnCancelListener {
            Log.d("TAG", "getCalendarEnd: calnceled")
        }
    }

    private fun getCalendarStart() {
        val datePicker = MaterialDatePicker.Builder
            .datePicker()
            .setTitleText("")
            .build()
        datePicker.show(childFragmentManager, "DatePicker")

        datePicker.addOnPositiveButtonClickListener {
            val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
            val date = dateFormatter.format(Date(it))
            binding.etTanggalMulai.text = date

        }

        datePicker.addOnNegativeButtonClickListener {
            Log.d("TAG", "getCalendarStart:Negative button")
        }

        datePicker.addOnCancelListener {
            Log.d("TAG", "getCalendarStart:Cancel button")
        }

    }

    private fun savePerizinan(savedUser: DataX?) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Apakah anda yakin ingin mengajukan Perizinan?")
            .setPositiveButton("Ya") { dialog, _ ->
                val nipRequestBody = MultipartBody.Part.createFormData("nip", savedUser!!.nip)
                val kodePerizinanRequestBody =
                    MultipartBody.Part.createFormData("kode_cuti", kode_perizinan!!)
                val tanggalMulaiRequestBody =
                    MultipartBody.Part.createFormData(
                        "tanggal_mulai",
                        binding.etTanggalMulai.text.toString()
                    )
                val tanggalSelesaiRequestBody =
                    MultipartBody.Part.createFormData(
                        "tanggal_selesai",
                        binding.etTanggalSelesai.text.toString()
                    )
                val keteranganRequestBody =
                    MultipartBody.Part.createFormData(
                        "keterangan",
                        binding.etKeterangan.text.toString()
                    )

                val bitmap: Bitmap = (binding.uploadFile.drawable as BitmapDrawable).bitmap
                val photo = uriToMultipartBody(bitmap)
                Log.d("sendData", savedUser!!.nip)
                viewModel.requestSendPermission(
                    nipRequestBody.body,
                    kodePerizinanRequestBody.body,
                    tanggalMulaiRequestBody.body,
                    tanggalSelesaiRequestBody.body,
                    photo,
                    keteranganRequestBody.body
                )

                viewModel.sendPermissionLiveData.observe(viewLifecycleOwner) {
                    when (it) {
                        is NetworkResult.Success -> {
                            binding.apply {
                                loadingInclude.loading.visibility = View.GONE
                                scrollView.visibility = View.VISIBLE
                            }
                            val response = it.data!!
                            val status = response.data.status
                            val messages = response.data.messages
                            if (status) {
                                val alertDialogHelper = AlertDialogHelper(requireContext())
                                alertDialogHelper.showAlertDialog("", messages)
                                findNavController().popBackStack()
//                                findNavController().navigate(R.id.action_formPerizinanFragment_to_menuPerizinanFragment)
                            }
                            val alertDialogHelper = AlertDialogHelper(requireContext())
                            alertDialogHelper.showAlertDialog("", messages)
                            findNavController().popBackStack()
//                            findNavController().navigate(R.id.action_formPerizinanFragment_to_menuPerizinanFragment)


//                            Log.d("pesan", messages)
//                                    requireActivity().onBackPressed()
                        }

                        is NetworkResult.Loading -> {
                            binding.apply {
                                loadingInclude.loading.visibility = View.VISIBLE
                                scrollView.visibility = View.GONE
                            }
                        }

                        is NetworkResult.Error -> {
                            binding.apply {
                                loadingInclude.loading.visibility = View.GONE
                                scrollView.visibility = View.VISIBLE
                            }
                            handleApiError(it.message)
                        }
                    }

                }
                Log.d("Uri", "null uri ${bitmap.toString()}")
            }

            .setNegativeButton("tidak")
            { dialog, _ ->
                dialog.cancel()
            }

        val alert = builder.create()
        alert.show()


    }

    private fun uriToMultipartBody(bitmap: Bitmap): MultipartBody.Part {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val requestFile = byteArray.toRequestBody("image/png".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", "image.png", requestFile)
    }

    override fun onConnectionAvailable() {
        super.onConnectionAvailable()
        binding.apply {
            toolbar.toolbar.visibility = View.VISIBLE
            scrollView.visibility = View.VISIBLE
            noInternetConnection.ivNoConnection.visibility = View.GONE
        }
    }

    override fun onConnectionLost() {
        super.onConnectionLost()
        binding.apply {
            toolbar.toolbar.visibility = View.GONE
            scrollView.visibility = View.GONE
            noInternetConnection.ivNoConnection.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        Log.d("TAG", "onResume: ")
        hideBottomNavigation()
        val myActivities = activity as MainActivity
        myActivities.hideMyBottomNav()
        super.onResume()
    }

    override fun onStart() {
        Log.d("TAG", "onStart: ")
        hideBottomNavigation()
        val myActivities = activity as MainActivity
        myActivities.hideMyBottomNav()
        super.onStart()
    }

    override fun onPause() {
        Log.d("TAG", "onPause: ")
        hideBottomNavigation()
        val myActivities = activity as MainActivity
        myActivities.hideMyBottomNav()
        super.onPause()
    }

    override fun onStop() {
        Log.d("TAG", "onStop: ")
        hideBottomNavigation()
        val myActivities = activity as MainActivity
        myActivities.hideMyBottomNav()
        super.onStop()
    }

    override fun onDestroyView() {
        Log.d("TAG", "onDestroyView: ")
//        showBottomNavigation()
        super.onDestroyView()
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("TAG", "onDetach: ")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("TAG", "onAttach: ")
    }

}