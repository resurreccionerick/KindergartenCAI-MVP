package com.example.myapplication.Teacher.Subject.Quiz

import android.content.Intent
import android.net.Uri

interface EditQuizContract {
    interface View {
        fun showSuccessMessage(message: String)
        fun showErrorMessage(message: String)
        fun onQuizUploaded()
        fun requestCameraPermission()
        fun requestStoragePermission()
        fun pickImageFromGallery()
        fun captureImageFromCamera()
    }

    interface Presenter {
        fun uploadEditQuiz(
            intent: Intent,
            quizId: String?,
            title: String?,
            opt1: String?,
            opt2: String?,
            opt3: String?,
            opt4: String?,
            correctAns: String,
            titleImage: Uri?
        )

        fun onCameraPermissionGranted()
        fun onStoragePermissionGranted()
    }
}