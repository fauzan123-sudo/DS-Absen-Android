package com.infinity.dsmabsen.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.infinity.dsmabsen.R
import com.infinity.dsmabsen.databinding.FragmentUbahPasswordBinding

class UbahPasswordFragment : DialogFragment() {

    private lateinit var binding: FragmentUbahPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUbahPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            // Inflate layout untuk dialog
            val dialogView = inflater.inflate(R.layout.fragment_ubah_password, null)

            // Set view untuk dialog
            builder.setView(dialogView)

            // Tambahkan tombol OK dan Cancel
            builder.setPositiveButton("OK") { dialog, which ->
                // Lakukan sesuatu ketika tombol OK ditekan
            }
            builder.setNegativeButton("Batal") { dialog, which ->
                dialog.dismiss()
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
//        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
//        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
//        dialog?.window?.setGravity(Gravity.CENTER)
//        binding.apply {
//            button.setOnClickListener {
//                val passwordLama = tvPasswordLama.text.toString()
//                val passwordBaru = tvPasswordBaru.text.toString()
//                if (passwordLama.isEmpty() || passwordBaru.isEmpty()) {
//                    tvPasswordLama.error = "harap isi bidang ini"
//                    tvPasswordLama.requestFocus()
//                    tvPasswordBaru.error = "harap isi bidang ini"
//                    tvPasswordBaru.requestFocus()
//                } else if (passwordLama == passwordBaru) {
//                    tvPasswordLama.error = ""
//                    tvPasswordLama.requestFocus()
//                } else {
//
//                }
//            }
//        }
//    }
}