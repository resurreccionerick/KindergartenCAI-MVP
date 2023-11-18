package com.example.myapplication.Teacher.Subject.Audio

import android.content.Intent
import android.net.Uri

class AddAudioPresenter(var view: AddAudioActivity) : AddAudioContract.Presenter {
    override fun uploadAudio(
        intent: Intent,
        title: String,
        audio: Uri,
        image: Uri
    ) {
        TODO("Not yet implemented")
    }

    override fun onCameraPermissionGranted() {
        view.captureImageFromCamera()
    }

    override fun onStoragePermissionGranted() {
        view.pickImageFromGallery()
    }

    override fun onAudioStoragePermissionGranted() {
        view.pickAudio()
    }
}