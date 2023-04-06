package com.infinity.dsmabsen.helper

import android.content.Context
import androidx.appcompat.app.AlertDialog

class AlertDialogHelper(private val context: Context) {
    fun showAlertDialog(title: String, message: String) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}

//cara panggil
//val alertDialogHelper = AlertDialogHelper(context)
//alertDialogHelper.showAlertDialog("Judul", "Isi pesan")
