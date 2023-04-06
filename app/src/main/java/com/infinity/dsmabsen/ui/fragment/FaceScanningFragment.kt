package com.infinity.dsmabsen.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.FaceDetector
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.FragmentFaceScanningBinding
import com.infinity.dsmabsen.databinding.FragmentScanWajahBinding
import com.infinity.dsmabsen.ui.activity.MainActivity
import com.infinity.dsmabsen.ui.viewModel.AttendanceViewModel


class FaceScanningFragment : Fragment() {
    private val viewModel: AttendanceViewModel by viewModels()
    private lateinit var binding: FragmentFaceScanningBinding
    private lateinit var cameraSource: CameraSource

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFaceScanningBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myActivities = activity as MainActivity
        myActivities.toast()
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                ScanWajahFragment.CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            startFaceDetection()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == ScanWajahFragment.CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startFaceDetection()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Camera permission is required for face detection",
                    Toast.LENGTH_SHORT
                ).show()
                requireActivity().finish()
            }
        }
    }

    private fun startFaceDetection() {
        val faceDetector = FaceDetector.Builder(requireContext())
            .setTrackingEnabled(false)
            .setLandmarkType(FaceDetector.ALL_LANDMARKS)
            .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
            .build()

        // create camera source
        cameraSource = CameraSource.Builder(requireContext(), faceDetector)
            .setRequestedPreviewSize(640, 480)
            .setFacing(CameraSource.CAMERA_FACING_FRONT)
            .setAutoFocusEnabled(true)
            .build()

        // add callback to surface holder
        binding.cameraPreview.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    cameraSource.start(binding.cameraPreview.holder)
                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        "Unable to start camera: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {

            }

            override fun surfaceDestroyed(p0: SurfaceHolder) {
                cameraSource.stop()
            }
        })
        // add face detection listener
        faceDetector.setProcessor(object : Detector.Processor<Face> {
            override fun release() {}

            override fun receiveDetections(detections: Detector.Detections<Face>) {
                val faces = detections.detectedItems
                if (faces.size() > 0) {
                    // face detected
                    requireActivity().runOnUiThread {
                        if (isAdded()) {
//                            val activity = requireActivity()
//                            activity.onBackPressed()

                            val data = "hasilScanWajah"
                            val action =
                                FaceScanningFragmentDirections.faceScanningToAttendance(data)
                            findNavController().navigate(action)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "fragment tidak dalam activity",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::cameraSource.isInitialized) {
            cameraSource.release()
        }
    }

}