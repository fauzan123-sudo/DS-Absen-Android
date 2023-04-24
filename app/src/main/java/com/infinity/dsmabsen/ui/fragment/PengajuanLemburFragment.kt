package com.infinity.dsmabsen.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.format.DateFormat
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.FragmentPengajuanLemburBinding
import com.infinity.dsmabsen.helper.handleApiError
import com.infinity.dsmabsen.model.DataX
import com.infinity.dsmabsen.repository.NetworkResult
import com.infinity.dsmabsen.ui.viewModel.AttendanceViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.infinity.dsmabsen.ui.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class PengajuanLemburFragment :
    BaseFragment<FragmentPengajuanLemburBinding>(FragmentPengajuanLemburBinding::inflate) {
    private val viewModel: AttendanceViewModel by viewModels()
    val savedUser = Paper.book().read<DataX>("user")

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            val myActivities = activity as MainActivity
            myActivities.hideMyBottomNav()

            tanggalPenggajuan.setOnClickListener {
                openCalendar()
            }

            jamMulai.setOnClickListener {
                openTimePickerFrom()
            }

            jamSelesai.setOnClickListener {
                openTimePickerUntil()
            }

            addImage.setOnClickListener {
                uploadFiles()
            }

        }
        setHasOptionsMenu(true)
        setupToolbar("Ajukan Lembur")
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.save -> {
                    var i = 0
                    if (binding.jamMulai.text.isEmpty()) {
                        binding.jamMulai.error = "Harap isi bidang ini!!"
                        binding.jamMulai.requestFocus()
                        i++
                    }
                    if (binding.jamSelesai.text.isEmpty()) {
                        binding.jamSelesai.error = "Harap isi bidang ini!!"
                        binding.jamSelesai.requestFocus()
                        i++

                    }
                    if (binding.tanggalPenggajuan.text.isEmpty()) {
                        binding.tanggalPenggajuan.error = "Harap isi bidang ini!!"
                        binding.tanggalPenggajuan.requestFocus()
                        i++

                    }
                    if (binding.keteranganPenggajuan.text.isEmpty()) {
                        binding.keteranganPenggajuan.error = "Harap isi bidang ini!!"
                        binding.keteranganPenggajuan.requestFocus()
                        i++
                    }

                    if (binding.imgUpload.drawable.constantState == resources.getDrawable(R.drawable.take_a_photo).constantState) {
                        AlertDialog.Builder(requireContext())
                            .setTitle("Peringatan!!")
                            .setMessage("ambil gambar")
                            //    .setIcon(R.drawable.ic_warning)
                            .setPositiveButton("Ok") { _, _ ->
                            }
                            .create().show()
                        i++
                    }
                    if (i == 0) {
                        saveLembur()
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
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(intent, CAMERA_REQUEST_CODE)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        putPhoto.launch(intent)
    }

    private fun choosePictureFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        putImage.launch(intent)
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private val putImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                try {
                    val imageUri = it?.data?.data
                    binding.imgUpload.setImageURI(imageUri)
                    binding.imgUpload.visibility = View.VISIBLE
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Gagal memuat gambar", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private val putPhoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                val bitmap = it?.data?.extras?.get("data") as Bitmap
                binding.imgUpload.setImageBitmap(bitmap)
                binding.imgUpload.visibility = View.VISIBLE
            } else if (it == null) {
//                binding.imgUpload.setImageResource(R.drawable.ic_launcher_background)
                binding.imgUpload.visibility = View.GONE
            } else {
                Log.d("TAG", "Task Cancelled")
//                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    private fun saveLembur() {
        val bitmap: Bitmap = (binding.imgUpload.drawable as BitmapDrawable).bitmap
        val photo = uriToMultipartBody(bitmap)
        val nipRequestBody = MultipartBody.Part.createFormData("nip", savedUser!!.nip)
        val jamMulaiRequestBody = MultipartBody.Part.createFormData("jam_mulai", savedUser!!.nip)
        val jamSelesaiRequestBody =
            MultipartBody.Part.createFormData("jam_selesai", savedUser!!.nip)
        val tanggalRequestBody = MultipartBody.Part.createFormData("tanggal", savedUser!!.nip)
        val keteranganRequestBody = MultipartBody.Part.createFormData("keterangan", savedUser!!.nip)
        viewModel.requestPengajuanLembur(
            nipRequestBody.body,
            jamMulaiRequestBody.body,
            jamSelesaiRequestBody.body,
            photo,
            tanggalRequestBody.body,
            keteranganRequestBody.body
        )
        viewModel.pengajuanLemburLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    val response = it.data!!
                    val status = response.status
                    val messages = response.data.messages
                    binding.apply {
                        binding.loadingInclude.loading.visibility = View.GONE
                        scrollView2.visibility = View.VISIBLE
                    }
                    if (messages != null) {
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setMessage(messages)
                            .setPositiveButton("Ya") { dialog, _ ->
                                dialog.cancel()
                            }
                            .setNegativeButton("Tidak") { dialog, _ ->
                                dialog.dismiss()
                            }
                        val alert = builder.create()
                        alert.show()
                    }
                    findNavController().popBackStack()
//                    requireActivity().onBackPressed()

                }

                is NetworkResult.Loading -> {
                    binding.apply {
                        binding.loadingInclude.loading.visibility = View.VISIBLE
                        scrollView2.visibility = View.GONE
                    }
                }

                is NetworkResult.Error -> {
                    binding.apply {
                        binding.loadingInclude.loading.visibility = View.GONE
                        scrollView2.visibility = View.VISIBLE
                    }
                    handleApiError(it.message)
                }
            }
        }
    }

    private fun uriToMultipartBody(bitmap: Bitmap): MultipartBody.Part {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val requestFile = byteArray.toRequestBody("image/png".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", "image.png", requestFile)
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

    private fun openCalendar() {
        val datePicker = MaterialDatePicker.Builder
            .datePicker()
            .setTitleText("")
            .build()
        datePicker.show(childFragmentManager, "DatePicker")

        datePicker.addOnPositiveButtonClickListener {
            val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
            val date = dateFormatter.format(Date(it))
            binding.tanggalPenggajuan.text = date
//            Toast.makeText(requireContext(), "$date is selected", Toast.LENGTH_LONG).show()

        }

        datePicker.addOnNegativeButtonClickListener {
            Log.d("TAG", "openCalendar: negative button")
        }

        datePicker.addOnCancelListener {
            Log.d("TAG", "openCalendar: Date Picker Cancelled ")
        }
    }

    private fun openTimePickerFrom() {
        val isSystem24Hour = DateFormat.is24HourFormat(requireContext())
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(8)
            .setMinute(0)
            .setTitleText("")
            .build()
        picker.show(childFragmentManager, "MyTag")

        picker.addOnPositiveButtonClickListener {
            var h = picker.hour
            var m = picker.minute
            if (h < 10 || m < 10) {
                h = String.format("0%02d", h).toInt()
            } else {
                h = String.format("%d", h).toInt()
            }
            m = String.format("%02d", m).toInt()

            var fixH = h.toString()
            var fixM = m.toString()
            if (h < 10) {
                fixH = "0$fixH"
            }
            if (m < 10) {
                fixM = "0$fixM"
            }
            binding.jamMulai.text = "$fixH:$fixM"
        }
    }

    private fun openTimePickerUntil() {
        val isSystem24Hour = DateFormat.is24HourFormat(requireContext())
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(8)
            .setMinute(0)
            .setTitleText("")
            .build()
        picker.show(childFragmentManager, "MyTag")

        picker.addOnPositiveButtonClickListener {
            var h = picker.hour
            var m = picker.minute
            if (h < 10) {
                h = String.format("%02d", h).toInt()
            } else {
                h = String.format("%d", h).toInt()
            }
            m = String.format("%02d", m).toInt()

            var fixH = h.toString()
            var fixM = m.toString()
            if (h < 10) {
                fixH = "0$fixH"
            }
            if (m < 10) {
                fixM = "0$fixM"
            }

            binding.jamSelesai.text = "$fixH:$fixM"
        }
    }

    override fun onConnectionAvailable() {
        super.onConnectionAvailable()
        binding.apply {
            toolbar.toolbar.visibility = View.VISIBLE
            scrollView2.visibility = View.VISIBLE
            noInternetConnection.ivNoConnection.visibility = View.GONE
        }
    }

    override fun onConnectionLost() {
        super.onConnectionLost()
        binding.apply {
            toolbar.toolbar.visibility = View.GONE
            scrollView2.visibility = View.GONE
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
        showBottomNavigation()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        hideBottomNavigation()
        Log.d("TAG", "onCreate: ")
    }


}