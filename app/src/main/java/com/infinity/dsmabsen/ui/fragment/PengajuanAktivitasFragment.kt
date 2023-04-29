package com.infinity.dsmabsen.ui.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.FragmentPengajuanAktivitasBinding
import com.infinity.dsmabsen.databinding.LayoutWarningDailogBinding
import com.infinity.dsmabsen.helper.AlertDialogHelper
import com.infinity.dsmabsen.helper.LocationUtils
import com.infinity.dsmabsen.helper.handleApiError
import com.infinity.dsmabsen.model.DataX
import com.infinity.dsmabsen.repository.NetworkResult
import com.infinity.dsmabsen.ui.activity.MainActivity
import com.infinity.dsmabsen.ui.viewModel.AktivitasViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class PengajuanAktivitasFragment :
    BaseFragment<FragmentPengajuanAktivitasBinding>(FragmentPengajuanAktivitasBinding::inflate) {
    private val viewModel: AktivitasViewModel by viewModels()
    val savedUser = Paper.book().read<DataX>("user")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar("Pengajuan Aktivitas")
        setHasOptionsMenu(true)
        binding.apply {
            val myActivities = activity as MainActivity
            myActivities.hideMyBottomNav()

            addImage.setOnClickListener {
                takePhoto()
            }

        }

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.save -> {
                    var i = 0
                    if (binding.edtKeterangan.text.isEmpty()) {
                        binding.edtKeterangan.error = "Harap isi bidang ini!!"
                        binding.edtKeterangan.requestFocus()
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
                        try {
                            LocationUtils.getActualLocation(requireContext(),
                                { latitude, longitude ->

                                    val koordinat = "$latitude $longitude"
                                    Log.d("koordinat", koordinat)
                                    saveAktivitas(koordinat)
                                },
                                {
                                    Toast.makeText(
                                        requireContext(),
                                        "Gagal mendapatkan lokasi",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                })
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun saveAktivitas(koordinat: String) {
//        val builder = AlertDialog.Builder(requireContext())
//        builder.setMessage("Apakah anda yakin ingin mengajukan Perizinan?")
//            .setPositiveButton("Ya") { dialog, _ ->
//                val bitmap: Bitmap = (binding.imgUpload.drawable as BitmapDrawable).bitmap
//                val nipRequestBody = MultipartBody.Part.createFormData("nip", savedUser!!.nip)
//                val photo = uriToMultipartBody(bitmap)
//                val namaBodyRequest =
//                    MultipartBody.Part.createFormData("nama", binding.edtKeterangan.text.toString())
//                val koordinatBodyRequest =
//                    MultipartBody.Part.createFormData("koordinat", koordinat)
//                viewModel.sendAktivitasRequest(
//                    nipRequestBody.body,
//                    namaBodyRequest.body,
//                    koordinatBodyRequest.body,
//                    photo
//                )
//                viewModel.sendAktivitasLiveData.observe(viewLifecycleOwner) {
//                    when (it) {
//                        is NetworkResult.Success -> {
//                            val response = it.data!!
//                            val status = response.status
//                            val messages = response.data.messages
//                            binding.apply {
//                                binding.loadingInclude.loading.visibility = View.GONE
//                                scrollView2.visibility = View.VISIBLE
//                            }
//                            if (messages != null) {
//                                val builder = AlertDialog.Builder(requireContext())
//                                builder.setMessage(messages)
//                                    .setPositiveButton("Ok") { dialog, _ ->
//                                        dialog.cancel()
//                                    }
//                                val alert = builder.create()
//                                alert.show()
//                            }
//                            findNavController().popBackStack()
//
//                        }
//
//                        is NetworkResult.Loading -> {
//                            binding.loadingInclude.loading.visibility = View.VISIBLE
//                            binding.scrollView2.visibility = View.GONE
//                        }
//
//                        is NetworkResult.Error -> {
//                            binding.loadingInclude.loading.visibility = View.GONE
//                            binding.scrollView2.visibility = View.VISIBLE
//                            handleApiError(it.message)
//                        }
//                    }
//                }
//            }
//            .setNegativeButton("Kembali") { _, _ ->
//                Toast.makeText(requireContext(), "Kembali", Toast.LENGTH_SHORT).show()
//            }
//            .create().show()

        val dialogBinding = LayoutWarningDailogBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
            .setView(dialogBinding.root)
            .create()

        dialogBinding.textTitle.text = "Pengajuan aerizinan"
        dialogBinding.textMessage.text =
            "Apakah anda yakin ingin mengajukan aktivitas?"
        dialogBinding.buttonYes.text = "Ya"
        dialogBinding.buttonNo.text = "tidak"
        dialogBinding.imageIcon.setImageResource(R.drawable.ic_baseline_warning_24)

        dialogBinding.buttonYes.setOnClickListener {
            val nipRequestBody = MultipartBody.Part.createFormData("nip", savedUser!!.nip)
            val namaBodyRequest =
                MultipartBody.Part.createFormData("nama", binding.edtKeterangan.text.toString())
            val koordinatRequestBody =
                MultipartBody.Part.createFormData(
                    "koordinat",
                    koordinat
                )

            val bitmap: Bitmap = (binding.imgUpload.drawable as BitmapDrawable).bitmap
            val photo = uriToMultipartBody(bitmap)
            Log.d("sendData", savedUser!!.nip)
            viewModel.sendAktivitasRequest(
                nipRequestBody.body,
                namaBodyRequest.body,
                koordinatRequestBody.body,
                photo
            )

            viewModel.sendAktivitasLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        binding.apply {
                            loadingInclude.loading.visibility = View.GONE
                            scrollView2.visibility = View.VISIBLE
                        }
                        val response = it.data!!
                        val status = response.data.status
                        val messages = response.data.messages
                        if (status == "Success") {
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
                            scrollView2.visibility = View.GONE
                        }
                    }

                    is NetworkResult.Error -> {
                        binding.apply {
                            loadingInclude.loading.visibility = View.GONE
                            scrollView2.visibility = View.VISIBLE
                        }
                        handleApiError(it.message)
                    }
                }

            }
            Log.d("Uri", "null uri $bitmap")

            builder.dismiss()
        }
        dialogBinding.buttonNo.setOnClickListener {
            builder.dismiss()
        }

        if (builder.window != null) {
            builder.window!!.setBackgroundDrawable(ColorDrawable(0))
        }

        builder.show()

    }


    private fun uriToMultipartBody(bitmap: Bitmap): MultipartBody.Part {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val requestFile = byteArray.toRequestBody("image/png".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("foto", "image.png", requestFile)
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

    private fun takePhoto() {
        val pictureDialog = AlertDialog.Builder(requireContext())
        pictureDialog.setTitle("Select Action")
        val pictureDialogItem = arrayOf(
            "Select photo from Gallery",
            "Capture photo from Camera"
        )
        pictureDialog.setItems(pictureDialogItem) { _, which ->
            when (which) {
                0 -> gallery()
                1 -> camera()
            }
        }

        pictureDialog.show()

    }

    private fun camera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        putPhoto.launch(intent)
    }

    private fun gallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageFromGalleryForResult.launch(intent)
    }

    private val putPhoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                val bitmap = it?.data?.extras?.get("data") as Bitmap
                binding.imgUpload.setImageBitmap(bitmap)
                binding.imgUpload.visibility = View.VISIBLE
            } else if (it == null) {
//                binding.imageView.setImageResource(R.drawable.ic_launcher_background)
                binding.imgUpload.visibility = View.GONE
            } else {
                Log.d("TAG", "Task Cancelled")
//                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }

        }

    private val pickImageFromGalleryForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {
            if (it.resultCode == Activity.RESULT_OK) {
                try {
                    val imageUri = it?.data?.data
                    binding.imgUpload.setImageURI(imageUri)
                    binding.imgUpload.visibility = View.VISIBLE
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Gagal memuat gambar", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

}