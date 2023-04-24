package com.infinity.dsmabsen.ui.fragment

import DataSpinnerAdapter
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.FragmentPengajuanReimbursementBinding
import com.infinity.dsmabsen.helper.handleApiError
import com.infinity.dsmabsen.model.DataX
import com.infinity.dsmabsen.model.DataXXXXXXXXXXXXXXXXXXXXXXXXXXX
import com.infinity.dsmabsen.repository.NetworkResult
import com.infinity.dsmabsen.ui.activity.MainActivity
import com.infinity.dsmabsen.ui.viewModel.ReimbursementViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class PengajuanReimbursementFragment :
    BaseFragment<FragmentPengajuanReimbursementBinding>(FragmentPengajuanReimbursementBinding::inflate) {
    private lateinit var adapter: DataSpinnerAdapter
    private lateinit var locationManager: LocationManager

    private val GALLERY_PERMISSION_CODE = 101

    // Konstanta untuk kode permintaan gambar
    private val CAMERA_REQUEST_CODE = 200
    private val GALLERY_REQUEST_CODE = 201

    private var selectedJenisReimbursement: DataXXXXXXXXXXXXXXXXXXXXXXXXXXX? = null
    var kodeReimbursement2: String? = null
    val viewModel: ReimbursementViewModel by viewModels()
    val savedUser = Paper.book().read<DataX>("user")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomNavigation()
        setHasOptionsMenu(true)
        setupToolbar("Ajukan Reimbursement")
        binding.apply {
            val myActivities = activity as MainActivity
            myActivities.hideMyBottomNav()
            addImage.setOnClickListener {
                uploadFiles()
            }

            viewModel.requestSpinnerReimbursement()
            viewModel.getSpinnerReimbursementLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        loadingInclude.loading.visibility = View.GONE
                        val response = it.data!!
                        val data = response.data
                        adapter = DataSpinnerAdapter(requireContext(), data)
                        spinnerJenisReimbursement.adapter = adapter
                        spinnerJenisReimbursement.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                                ) {
                                    val clickedItem: DataXXXXXXXXXXXXXXXXXXXXXXXXXXX =
                                        parent?.getItemAtPosition(position) as DataXXXXXXXXXXXXXXXXXXXXXXXXXXX
                                    val kodeReimbursement = clickedItem.kode_reimbursement
                                    kodeReimbursement2 = kodeReimbursement
                                }

                                override fun onNothingSelected(p0: AdapterView<*>?) {
                                    selectedJenisReimbursement = null
                                }

                            }
                    }

                    is NetworkResult.Loading -> {
                        loadingInclude.loading.visibility = View.VISIBLE

                    }

                    is NetworkResult.Error -> {
                        loadingInclude.loading.visibility = View.GONE
                        handleApiError(it.message)
                    }
                }
            }

        }
        toolbar.setOnMenuItemClickListener { menuItem ->
            var i = 0
            when (menuItem.itemId) {
                R.id.save -> {
                    if (binding.edtNominal.text.isEmpty()) {
                        binding.edtNominal.error = "Harap isi bidang ini!!"
                        binding.edtNominal.requestFocus()
                        i++
                    }
                    if (binding.edtKeterangan.text.isEmpty()) {
                        binding.edtKeterangan.error = "Harap isi bidang ini!!"
                        binding.edtKeterangan.requestFocus()
                        i++
                    }
                    if (binding.imgUpload.drawable.constantState == resources.getDrawable(R.drawable.take_a_photo).constantState) {
                        AlertDialog.Builder(requireContext())
                            .setTitle("Peringatan!!")
                            .setMessage("Silahkan ambil gambar terlebih dahulu")
                            //    .setIcon(R.drawable.ic_warning)
                            .setPositiveButton("Ok") { _, _ ->
                            }
                            .create().show()
                        i++
                    }

//                    if (args.codeBarcode == "" || args.codeBarcode.isEmpty()) {
//                        Toast.makeText(requireContext(), "args kosong", Toast.LENGTH_SHORT).show()
//                        i++
//                    }

                    if (i == 0) {
                        saveReimbursement(savedUser)
                    }
                    true
                }
//
//                R.id.scan -> {
//                    scanBarcode()
//                    true
//                }

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
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(intent, CAMERA_REQUEST_CODE)
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
//                binding.imageView.setImageResource(R.drawable.ic_launcher_background)
                binding.imgUpload.visibility = View.GONE
            } else {
                Log.d("TAG", "Task Cancelled")
//                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        val menuSave = menu.findItem(R.id.save)
        val menuPlus = menu.findItem(R.id.add)
        val menuLogout = menu.findItem(R.id.logout)
        val menuScan = menu.findItem(R.id.scan)

        menuScan.isVisible = false
        menuLogout.isVisible = false
        menuSave?.isVisible = true
        menuPlus?.isVisible = false

    }

    private fun saveReimbursement(savedUser: DataX?) {

        Log.d("click send reimbursement", "here")
        AlertDialog.Builder(requireContext())
            .setMessage("Anda yakin ingin mengajukan reimbursement")
            //    .setIcon(R.drawable.ic_warning)
            .setPositiveButton("Ya") { _, _ ->
                val nipRequestBody = MultipartBody.Part.createFormData("nip", savedUser!!.nip)
                val kodeReimbursementRequestBody =
                    MultipartBody.Part.createFormData("kode_reimbursement", kodeReimbursement2!!)
                val nominalRequestBody =
                    MultipartBody.Part.createFormData(
                        "nilai",
                        binding.edtNominal.text.toString()
                    )
                val keteranganRequestBody =
                    MultipartBody.Part.createFormData(
                        "keterangan",
                        binding.edtKeterangan.text.toString()
                    )

                val bitmap: Bitmap = (binding.imgUpload.drawable as BitmapDrawable).bitmap
                val photo = uriToMultipartBody(bitmap)
                Log.d("sendData", savedUser!!.nip)
                Log.d(
                    "saveReimbursement",
                    "nip = $nipRequestBody \n " +
                            "kodeReimbursement = $kodeReimbursementRequestBody \n" +
                            "nominal = $nominalRequestBody \n" +
                            "photo =  $photo \n" +
                            "keterangan = $keteranganRequestBody"
                )
                viewModel.requestPengajuanReimbursement(
                    nipRequestBody.body,
                    kodeReimbursementRequestBody.body,
                    nominalRequestBody.body,
                    photo,
                    keteranganRequestBody.body
                )
                viewModel.getPengajuanLiveData.observe(viewLifecycleOwner) {
                    when (it) {
                        is NetworkResult.Success -> {
                            binding.apply {
                                binding.loadingInclude.loading.visibility = View.GONE
                                scrollView2.visibility = View.VISIBLE
                            }
                            val response = it.data!!
                            val messages = response.data.messages
                            val builders = AlertDialog.Builder(requireContext())
                            builders.setMessage(messages)
                                .setNegativeButton("Ya") { dialog, _ ->
                                    dialog.cancel()
                                }
                            val alert = builders.create()
                            alert.show()
                            findNavController().popBackStack()
//                            requireActivity().onBackPressed()
                        }

                        is NetworkResult.Loading -> {
                            binding.apply {
                                scrollView2.visibility = View.GONE
                                binding.loadingInclude.loading.visibility = View.VISIBLE
                            }
                        }

                        is NetworkResult.Error -> {
                            binding.apply {
                                scrollView2.visibility = View.VISIBLE
                                binding.loadingInclude.loading.visibility = View.GONE
                            }
                            handleApiError(it.message)
                        }
                    }
                }
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            .create().show()
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