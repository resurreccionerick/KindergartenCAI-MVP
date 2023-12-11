package com.example.myapplication.Teacher.Subject.Audio

import android.content.Intent
import android.net.Uri

interface AddAudioContract {
    interface View {
        fun onAudioUploaded()
        fun showSuccessMessage(message: String)
        fun showErrorMessage(message: String)
        fun requestCameraPermission()
        fun requestStoragePermission()
        fun requestAudioStoragePermission()
        fun pickAudio()
        fun pickImageFromGallery()
        fun captureImageFromCamera()
    }

    interface Presenter {
        fun uploadAudio(
            intent: Intent,
            subjectId: String,
            title: String,
            audio: Uri,
            image: Uri
        )

        fun onCameraPermissionGranted()
        fun onStoragePermissionGranted()
        fun onAudioStoragePermissionGranted()
    }
}