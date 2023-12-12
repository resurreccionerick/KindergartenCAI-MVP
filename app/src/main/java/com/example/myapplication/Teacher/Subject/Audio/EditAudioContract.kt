package com.example.myapplication.Teacher.Subject.Audio

import android.content.Intent
import android.net.Uri

interface EditAudioContract {

    interface View {
        fun showMessage(message: String)
        fun onAudioUploaded()
        fun requestCameraPermission()
        fun requestStoragePermission()
        fun requestAudioStoragePermission()
        fun showImageSourceDialog()
        fun pickAudio()
        fun pickImageFromGallery()
        fun captureImageFromCamera()
    }

    interface Presenter {
        fun uploadEditAudio(
            audioId: String,
            intent: Intent,
            subjId: String?,
            title: String?,
            audioUrl: Uri?,
            image: Uri?
        )

        fun onCameraPermissionGranted()
        fun onStoragePermissionGranted()
        fun onAudioStoragePermissionGranted()
    }
}