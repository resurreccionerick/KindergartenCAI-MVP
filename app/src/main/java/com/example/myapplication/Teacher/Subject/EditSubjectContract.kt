package com.example.myapplication.Teacher.Subject

import android.content.Intent
import android.net.Uri

interface EditSubjectContract {

    interface View {
        fun showSuccessMessage(message: String)
        fun showErrorMessage(message: String)
        fun onSubjectUploaded()
        fun requestCameraPermission()
        fun requestStoragePermission()
        fun pickImageFromGallery()
        fun captureImageFromCamera()
    }

    interface Presenter {
        fun uploadEditSubject(intent: Intent, id: String?, title: String?, image: Uri?)
        fun onCameraPermissionGranted()
        fun onStoragePermissionGranted()
        //fun pickImageFromGallery()
    }
}