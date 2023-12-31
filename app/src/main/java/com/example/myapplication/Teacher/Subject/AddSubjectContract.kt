package com.example.myapplication.Teacher.Subject

import android.content.Intent
import android.net.Uri
import com.example.myapplication.Models.Subject

interface AddSubjectContract {
    interface View {
        fun onSubjectUploaded()
        fun showSuccessMessage(message: String)
        fun showErrorMessage(message: String)
        fun requestCameraPermission()
        fun requestStoragePermission()
        fun pickImageFromGallery()
        fun captureImageFromCamera()
    }

    interface Presenter {
        fun uploadSubject(intent: Intent, title: String, imageUrl: Uri)

        fun onCameraPermissionGranted()
        fun onStoragePermissionGranted()

    }
}
