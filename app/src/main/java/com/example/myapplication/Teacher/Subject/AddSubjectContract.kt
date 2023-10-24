package com.example.myapplication.Teacher.Subject

import android.net.Uri

interface AddSubjectContract {
    interface View {
        fun onSubjectUploaded()
        fun showSuccessMessage(message: String)
        fun showErrorMessage(message: String)
        fun requestCameraPermission()
        fun requestStoragePermission()
        fun pickImageFromGallery()
    }

    interface Presenter {
        fun uploadSubject(title: String, imageUrl: Uri)
        fun onUploadButtonClick()
        fun onCameraPermissionGranted()
        fun onStoragePermissionGranted()
    }
}
