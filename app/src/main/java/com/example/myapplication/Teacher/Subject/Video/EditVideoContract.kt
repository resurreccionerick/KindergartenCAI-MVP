package com.example.myapplication.Teacher.Subject.Video

import android.content.Intent
import android.net.Uri

interface EditVideoContract {

    interface View {
        fun showSuccessMessage(message: String)
        fun showErrorMessage(message: String)
        fun onVideoUploaded()
        fun requestCameraPermission()
        fun requestStoragePermission()
        fun pickVideoFromGallery()
        fun captureVideoFromCamera()
    }

    interface Presenter {
        fun uploadEditVideo(intent: Intent, subjId: String?, title: String?, image: Uri?)
        fun onCameraPermissionGranted()
        fun onStoragePermissionGranted()
    }

}