package com.example.myapplication.Teacher.Subject.Quiz

import android.content.Intent
import android.net.Uri

interface AddQuizContract {
    interface View {
        fun onQuizUploaded()
        fun showSuccessMessage(message: String)
        fun showErrorMessage(message: String)
        fun requestCameraPermission()
        fun requestStoragePermission()
        fun pickImageFromGallery()
        fun captureImageFromCamera()
    }

    interface Presenter {
        fun uploadQuiz(
            intent: Intent,
            title: String,
            opt1: String,
            opt2: String,
            opt3: String,
            opt4: String,
            imgTitleUrl: Uri
        )

        fun onCameraPermissionGranted()
        fun onStoragePermissionGranted()
    }
}