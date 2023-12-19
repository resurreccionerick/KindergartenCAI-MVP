package com.example.myapplication.Admin.Student.Register

import android.content.Intent
import android.net.Uri

interface StudentRegisterContract {

    interface View {
        fun onSuccess(message: String);
        fun onFailure(message: String);
        fun onStudentUploaded()
        fun requestCameraPermission()
        fun requestStoragePermission()
        fun pickImageFromGallery()
        fun captureImageFromCamera()
    }

    interface Presenter {
        fun doRegister(email: String, password: String, title: String, intent: Intent, selectedImageUri: Uri?);
        fun onCameraPermissionGranted()
        fun onStoragePermissionGranted()
    }
}