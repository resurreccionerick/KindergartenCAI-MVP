package com.example.myapplication.Teacher.Subject.Video

import android.content.Intent
import android.net.Uri


interface AddVideoContract {
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
        fun uploadVideo(intent: Intent, subjectId: String?, title: String?, video: Uri)
        fun onCameraPermissionGranted()
        fun onStoragePermissionGranted()
    }
}