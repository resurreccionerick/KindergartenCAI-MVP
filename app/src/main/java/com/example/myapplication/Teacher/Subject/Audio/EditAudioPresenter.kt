package com.example.myapplication.Teacher.Subject.Audio

import android.content.Intent
import android.net.Uri
import com.example.myapplication.Models.Audio
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class EditAudioPresenter(var view: EditAudioActivity) : EditAudioContract.Presenter {
    override fun uploadEditAudio(
        audioId: String,
        intent: Intent,
        subjectId: String?,
        title: String?,
        audioUri: Uri?,
        imageUri: Uri?
    ) {
        val audio = Audio(subjectId, title, "", "")

        val databaseReference =
            FirebaseDatabase.getInstance().getReference("Subjects").child(subjectId.toString())
                .child("Audio")

        val storageReference = FirebaseStorage.getInstance().reference

        val audioId = audioId

        if (audioUri != null && imageUri != null) { // Check if AudioUri & imageUri is not null
            val audioRef: StorageReference =
                storageReference.child("subject_audio/${audioId}.mp3")

            val audioImgRef: StorageReference =
                storageReference.child("subject_audio_img/${audioId}.jpeg")

            val uploadTask: UploadTask = audioRef.putFile(audioUri)
            val uploadTaskImg: UploadTask = audioImgRef.putFile(imageUri)

            uploadTask.addOnSuccessListener { taskSnapshot ->
                // Audio upload success
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    val audioUrl = uri.toString()
                    audio.id = audioId
                    audio.audioUrl = audioUrl // Update the Audio URL

                    val audioReference = databaseReference.child(audioId)
                    audioReference.setValue(audio) // Update the data in Firebase Database

                    view.showMessage("Audio Successfully Updated.")
                }
            }.addOnFailureListener {
                // Audio upload failure
                view.showMessage("Audio upload failed")
            }

            uploadTaskImg.addOnSuccessListener { taskSnapshot ->
                // Audio upload success
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    val audioImgUrl = uri.toString()
                    audio.audioPic = audioImgUrl // Update the Audio img

                    val audioReference = databaseReference.child(audioId)
                    audioReference.setValue(audio) // Update the data in Firebase Database

                    view.onAudioUploaded()
                }
            }.addOnFailureListener {
                // Audio upload failure
                view.showMessage("Image upload failed")
            }
        }
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