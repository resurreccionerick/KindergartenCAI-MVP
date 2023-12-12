package com.example.myapplication.Teacher.Subject.Audio

import android.content.Intent
import android.net.Uri
import com.example.myapplication.Models.Audio

interface AddAudioContract {
    //for adding audio
    interface AddAudioView {
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


    //for the audio activity
    interface AudioView {
        fun showMessage(message: String)
        fun showAudio(audio: List<Audio>)
    }

    interface AudioPresenter {
        fun loadAudio(id: String)
        fun deleteAudio(subjID: String, audioID: String, audio: Audio)
    }

}